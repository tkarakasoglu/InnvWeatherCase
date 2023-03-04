package com.tk.innovaweathercase.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tk.innovaweathercase.data.repository.WeatherRepository

class WeatherViewModelFactory(
    private val weatherRepository: WeatherRepository
    ): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WeatherViewModel(weatherRepository) as T
    }
}