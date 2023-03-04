package com.tk.innovaweathercase.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tk.innovaweathercase.data.model.City
import com.tk.innovaweathercase.util.ConstantsDatabase.Companion.WEATHER_DATABASE_CITY_TABLE

@Dao
interface WeatherDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun upsertCityList(cityList: List<City>): List<Long>

    @Query("UPDATE $WEATHER_DATABASE_CITY_TABLE SET isFav = :isFav WHERE geonameId = :cityGeonameId")
    suspend fun setCityFavorites(cityGeonameId: Int, isFav: Boolean)

    @Query("SELECT * FROM $WEATHER_DATABASE_CITY_TABLE ORDER BY isFav DESC, toponymName")
    suspend fun getCityList(): List<City>

    @Query("SELECT * FROM $WEATHER_DATABASE_CITY_TABLE WHERE isCurrent OR isFav")
    suspend fun getCityFavoritesList(): List<City>

    @Query("SELECT * FROM $WEATHER_DATABASE_CITY_TABLE  WHERE toponymName = :cityName")
    suspend fun getCityFromName(cityName: String): City

    @Query("UPDATE $WEATHER_DATABASE_CITY_TABLE SET isCurrent = 0")
    suspend fun resetCurrentCity()

    @Query("UPDATE $WEATHER_DATABASE_CITY_TABLE SET isCurrent = 1 WHERE toponymName = :cityName")
    suspend fun setCurrentCityFromName(cityName: String)
}