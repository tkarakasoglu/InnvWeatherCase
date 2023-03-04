package com.tk.innovaweathercase.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tk.innovaweathercase.data.model.City
import com.tk.innovaweathercase.util.ConstantsDatabase

@Database(
    entities = [City::class],
    version = 1
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun getWeatherDAO(): WeatherDAO

    companion object {
        @Volatile
        private var instance: WeatherDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                WeatherDatabase::class.java,
                ConstantsDatabase.WEATHER_DATABASE_FILE
            ).build()
    }
}