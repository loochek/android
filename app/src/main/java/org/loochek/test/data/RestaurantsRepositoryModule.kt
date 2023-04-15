package org.loochek.test.data

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RestaurantsRepositoryModule {
    @Provides
    @Singleton
    fun provideRestaurantRepository(
        httpClient: HttpClient,
        dbDao: RestaurantsDAO
    ): RestaurantsRepository {
        Log.i("provideRestaurantRepository", ".")
        return RestaurantsRepository(httpClient, dbDao)
    }
}