package com.tk.innovaweathercase.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tk.innovaweathercase.data.model.City
import com.tk.innovaweathercase.data.model.WeatherAPIResponse
import com.tk.innovaweathercase.data.repository.WeatherRepository
import com.tk.innovaweathercase.util.Constants
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val weatherRepository: WeatherRepository
): ViewModel() {

    val cityListMutableLiveData = MutableLiveData<List<City>>()
    val cityFavoritesMutableLiveData = MutableLiveData<List<City>>()
    val weatherMutableLiveDataHashMap = HashMap<Int, MutableLiveData<WeatherAPIResponse>>()

    init {
        getCityList()
    }

    private fun getCityList() = viewModelScope.launch {
        val cityList = weatherRepository.getLiveCityList()

        // Add MutableLiveData for cities
        for (city in cityList) {
            weatherMutableLiveDataHashMap[city.geonameId] = MutableLiveData<WeatherAPIResponse>()
        }

        cityListMutableLiveData.postValue(cityList)

        updateCityFavoritesList()
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

    fun getWeatherInformation(city: City) = viewModelScope.launch {
        val weatherAPIResponse = weatherRepository.getWeatherInformation(city.lat, city.lng)
        weatherMutableLiveDataHashMap[city.geonameId]?.postValue(weatherAPIResponse)
    }

    private suspend fun updateCityFavoritesList() {
        val cityFavoritesList = weatherRepository.getCityFavoritesList()

        // If there is no favorites, add İzmir instead
        val cityList = cityFavoritesList.ifEmpty {
            listOf(weatherRepository.getCityFromName(Constants.DEFAULT_CITY))
        }

        cityFavoritesMutableLiveData.postValue(cityList)
    }
}