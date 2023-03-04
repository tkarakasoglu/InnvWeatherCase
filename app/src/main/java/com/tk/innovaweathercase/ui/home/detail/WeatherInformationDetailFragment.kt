package com.tk.innovaweathercase.ui.home.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tk.innovaweathercase.data.model.City
import com.tk.innovaweathercase.databinding.FragmentWeatherinformationdetailBinding
import com.tk.innovaweathercase.ui.WeatherActivity
import com.tk.innovaweathercase.ui.WeatherViewModel
import kotlin.math.roundToInt

class WeatherInformationDetailFragment(private val city: City) : Fragment() {
    lateinit var weatherViewModel: WeatherViewModel

    private var _binding: FragmentWeatherinformationdetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherinformationdetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initUI()
        initViewModel()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        binding.cityNameTextView.text = city.toponymName
    }

    private fun initViewModel() {
        weatherViewModel = (activity as WeatherActivity).weatherViewModel
        weatherViewModel.getWeatherInformation(city)

        weatherViewModel.weatherMutableLiveDataHashMap[city.geonameId]?.observe(viewLifecycleOwner, Observer { response ->
            val currentTemperature = response.main.temp.roundToInt().toString() + "°C"
            val feelsLikeTemperature = response.main.feels_like.roundToInt().toString() + "°C"
            val pressure = response.main.pressure.toString() + " hPa"
            val humidity = response.main.humidity.toString() + "%"
            binding.currentTemperatureTextView.text = currentTemperature
            binding.feelsLikeTextView.text = feelsLikeTemperature
            binding.pressureTextView.text = pressure
            binding.humidityTextView.text = humidity
        })
    }
}