package com.tk.innovaweathercase.data.api.weather

import com.tk.innovaweathercase.data.model.WeatherAPIResponse
import com.tk.innovaweathercase.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("weather")
    suspend fun getWeather(
        @Query("lat")
        lat: String,
        @Query("lon")
        lon: String,
        @Query("units")
        units: String = "metric",
        @Query("appid")
        appid: String = Constants.WEATHER_API_APP_ID,
    ): Response<WeatherAPIResponse>
}