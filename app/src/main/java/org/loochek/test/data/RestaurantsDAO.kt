package org.loochek.test.data

import androidx.room.*
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