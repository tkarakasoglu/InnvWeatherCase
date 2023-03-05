package com.tk.innovaweathercase.data.repository

import android.content.Context
import com.tk.innovaweathercase.data.api.cities.CitiesAPIRetrofitInstance
import com.tk.innovaweathercase.data.api.weather.WeatherAPIRetrofitInstance
import com.tk.innovaweathercase.data.db.WeatherDatabase
import com.tk.innovaweathercase.data.model.City
import com.tk.innovaweathercase.data.model.WeatherAPIResponse
import com.tk.innovaweathercase.data.model.WeatherInformation
import com.tk.innovaweathercase.util.Utilities

class WeatherRepository(
    private val context: Context,
    private val db: WeatherDatabase
) {
    suspend fun getLiveCityList(): List<City> {
        if (Utilities.isInternetAvailable(context)) {
            val result = CitiesAPIRetrofitInstance.api.getCities()
            if (result.body() != null) {
                db.getWeatherDAO().upsertCityList(result.body()!!.geonames)
            }
        }

        return getOfflineCityList()
    }

    suspend fun getOfflineCityList() = db.getWeatherDAO().getCityList()

    suspend fun setCityFavorites(city: City, isFav: Boolean) = db.getWeatherDAO().setCityFavorites(city.geonameId, isFav)

    suspend fun getCityFavoritesList() = db.getWeatherDAO().getCityFavoritesList()

    suspend fun getCityFromName(cityName: String) = db.getWeatherDAO().getCityFromName(cityName)

    suspend fun resetCurrentCity() = db.getWeatherDAO().resetCurrentCity()

    suspend fun setCurrentCityFromName(cityName: String) = db.getWeatherDAO().setCurrentCityFromName(cityName)

    suspend fun getWeatherInformation(lat: String, lon: String): WeatherAPIResponse {
        if (Utilities.isInternetAvailable(context)) {
            val result = WeatherAPIRetrofitInstance.api.getWeather(lat, lon)
            if (result.body() != null) {
                return result.body()!!
            }
        }

        return WeatherAPIResponse()
    }

}