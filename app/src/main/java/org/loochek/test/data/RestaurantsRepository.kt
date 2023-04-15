package org.loochek.test.data

import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class RestaurantsRepository @Inject constructor(
    private val httpClient: HttpClient,
    private val dbDao: RestaurantsDAO
) {
    private var updatedOnce = false

    suspend fun getCatalogResponse(forceUpdate: Boolean = false): Flow<RestaurantCatalogResponse> = flow {
        // Return cached version
        emit(RestaurantCatalogResponse(
            dbDao.getNearestRestaurants(),
            dbDao.getPopularRestaurants(),
            null,
            actual = updatedOnce,
            updateError = false
        ))

        if (!updatedOnce || forceUpdate) {
            // Try to fetch actual version
            try {
                var actualResponse = httpClient.get("http://195.2.84.138:8081/catalog")
                    .body<RestaurantCatalogResponse>()
                actualResponse = actualResponse.copy(
                    nearest=actualResponse.nearest.map {
                        it.copy(placement = RestaurantPlacement.Nearest)
                    },
                    popular=actualResponse.popular.map {
                        it.copy(
                            placement = RestaurantPlacement.Popular,
                            id = it.id + 10
                        )
                    }
                )

                emit(actualResponse.copy(actual = true, updateError = false))

                dbDao.deleteAllRestaurants()
                dbDao.insertRestaurants(*actualResponse.nearest.toTypedArray())
                dbDao.insertRestaurants(*actualResponse.popular.toTypedArray())

                updatedOnce = true;
            } catch (e: Exception) {
                emit(RestaurantCatalogResponse(listOf(), listOf(), null,
                    actual = false,
                    updateError = true
                ))
            }
        }
    }
}