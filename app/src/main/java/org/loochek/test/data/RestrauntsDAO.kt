package org.loochek.test.data

import android.content.Context
import androidx.room.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Dao
interface RestaurantsDAO {
    @Query("SELECT * FROM restaurants WHERE placement = 'Popular'")
    fun getPopularRestaurants(): List<Restaurant>

    @Query("SELECT * FROM restaurants WHERE placement = 'Nearest'")
    fun getNearestRestaurants(): List<Restaurant>

    @Insert
    fun insertRestaurants(vararg restaurants: Restaurant)

    @Query("DELETE FROM restaurants")
    fun deleteAllRestaurants()
}

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