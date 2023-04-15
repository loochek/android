package org.loochek.test.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RestaurantsDatabaseModule {
    @Provides
    fun provideRestaurantsDatabase(@ApplicationContext applicationContext: Context) =
        Room.databaseBuilder(
            applicationContext,
            RestaurantsDatabase::class.java,
            "food-delivery-db"
        ).build()

    @Provides
    fun provideRestaurantsDao(db: RestaurantsDatabase): RestaurantsDAO = db.restaurantsDao()
}