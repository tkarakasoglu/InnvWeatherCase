package com.tk.innovaweathercase.data.api.cities;

import com.tk.innovaweathercase.data.model.CitiesAPIResponse

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query

interface CitiesAPI {

    @GET("searchJSON")
    suspend fun getCities(
        @Query("username")
        username: String = "ksuhiyp",
        @Query("country")
        country: String = "tr",
        @Query("style")
        style: String = "SHORT",
        @Query("featureCode")
        featureCode: String = "PPLA",
    ): Response<CitiesAPIResponse>
}
