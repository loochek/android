package org.loochek.test.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Database(entities = [Restaurant::class], version = 1)
abstract class RestaurantsDatabase : RoomDatabase() {
    abstract fun restaurantsDao(): RestaurantsDAO
}

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