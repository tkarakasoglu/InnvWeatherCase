package com.tk.innovaweathercase.data.model

data class CitiesAPIResponse(
    val geonames: List<City>,
    val totalResultsCount: Int
)