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
import javax.inject.Singleton

@Database(entities = [Restaurant::class], version = 1)
abstract class RestaurantsDatabase : RoomDatabase() {
    abstract fun restaurantsDao(): RestaurantsDAO
}