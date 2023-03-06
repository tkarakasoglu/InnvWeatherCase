package com.tk.innovaweathercase.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tk.innovaweathercase.data.model.City
import com.tk.innovaweathercase.data.model.WeatherAPIResponse
import com.tk.innovaweathercase.data.repository.WeatherRepository
import com.tk.innovaweathercase.util.Constants
import com.tk.innovaweathercase.util.Utilities
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val app: Application,
    private val weatherRepository: WeatherRepository
): AndroidViewModel(app) {

    val cityListMutableLiveData = MutableLiveData<List<City>>()
    val cityFavoritesMutableLiveData = MutableLiveData<List<City>>()
    val weatherMutableLiveDataHashMap = HashMap<Int, MutableLiveData<WeatherAPIResponse>>()
    val isAppGPSEnabledLiveData = MutableLiveData<Boolean>()

    init {
        setup()
    }

    private fun setup() = viewModelScope.launch {
        // Get City list online
        val cityList = weatherRepository.getLiveCityList()

        // Add MutableLiveData for cities
        for (city in cityList) {
            weatherMutableLiveDataHashMap[city.geonameId] = MutableLiveData<WeatherAPIResponse>()
        }

        cityListMutableLiveData.postValue(cityList)

        // Set app GPS
        isAppGPSEnabledLiveData.postValue(Utilities.checkAppGPSAndEnableForFistRun(app))
    }

    fun setCityFavorites(city: City, isFav: Boolean) = viewModelScope.launch {
        weatherRepository.setCityFavorites(city, isFav)

        // Update RecyclerView city list
        val cityListOffline = weatherRepository.getOfflineCityList()
        cityListMutableLiveData.postValue(cityListOffline)

        // Update CityFavoritesList
        updateCityFavoritesList()
    }

    fun resetCurrentCity() = viewModelScope.launch {
        weatherRepository.resetCurrentCity()

        // Update CityFavoritesList
        updateCityFavoritesList()
    }

    fun setCurrentCityFromName(cityName: String) = viewModelScope.launch {
        weatherRepository.resetCurrentCity()
        weatherRepository.setCurrentCityFromName(cityName)

        // Update CityFavoritesList
        updateCityFavoritesList()
    }

    fun setAppGPSEnabled(enabled: Boolean) = viewModelScope.launch {
        isAppGPSEnabledLiveData.postValue(enabled)
    }

    fun getWeatherInformation(city: City) = viewModelScope.launch {
        val weatherAPIResponse = weatherRepository.getWeatherInformation(city.lat, city.lng)
        weatherMutableLiveDataHashMap[city.geonameId]?.postValue(weatherAPIResponse)
    }

    private suspend fun updateCityFavoritesList() {
        // Clear list
        cityFavoritesMutableLiveData.postValue(listOf())

        val cityFavoritesList = weatherRepository.getCityFavoritesList()

        // If there is no favorites, add İzmir instead
        val cityList = cityFavoritesList.ifEmpty {
            val cityDefault = weatherRepository.getCityFromName(Constants.DEFAULT_CITY)
            if (cityDefault != null) {
                listOf(cityDefault)
            } else {
                listOf()
            }
        }

        cityFavoritesMutableLiveData.postValue(cityList)
    }
}