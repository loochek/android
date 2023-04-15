package org.loochek.test.data

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

@Module
@InstallIn(SingletonComponent::class)
class HTTPClientModule {
    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    fun provideHttpClient(): HttpClient {
        return HttpClient() {
            install(ContentNegotiation) {
                json(Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                    explicitNulls = false
                })
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 5000
            }
        }
    }
}