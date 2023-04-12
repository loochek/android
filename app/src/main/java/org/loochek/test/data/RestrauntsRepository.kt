package org.loochek.test.data

import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class RestrauntsRepository @Inject constructor(
    private val httpClient: HttpClient,
    private val dbDao: RestaurantsDAO
) {
    suspend fun getCatalogResponse(): Flow<RestaurantCatalogResponse>  = flow {
        // Return cached version
        emit(RestaurantCatalogResponse(
            dbDao.getNearestRestaurants(),
            dbDao.getPopularRestaurants(),
            null,
            actual = false,
            updateError = false
        ))

        // Try to fetch actual version
        try {
            val actualResponse = httpClient.get("http://195.2.84.138:8081/catalog")
                .body<RestaurantCatalogResponse>()
            emit(actualResponse.copy(actual = true, updateError = false))

            dbDao.deleteAllRestaurants()
            dbDao.insertRestaurants(*actualResponse.nearest.map {it.copy(placement = RestaurantPlacement.Nearest)}.toTypedArray())
            dbDao.insertRestaurants(*actualResponse.popular.map {it.copy(placement = RestaurantPlacement.Popular, id = it.id + 10)}.toTypedArray())
        } catch (e: Exception) {
            Log.i("Aboba", e.toString())
            emit(RestaurantCatalogResponse(listOf(), listOf(), null,
                actual = false,
                updateError = true
            ))
        }
    }
}