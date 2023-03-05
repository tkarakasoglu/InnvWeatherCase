package com.tk.innovaweathercase.ui

import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tk.innovaweathercase.R
import com.tk.innovaweathercase.data.db.WeatherDatabase
import com.tk.innovaweathercase.data.repository.WeatherRepository
import com.tk.innovaweathercase.databinding.ActivityWeatherBinding
import com.tk.innovaweathercase.util.Utilities
import java.util.*

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherBinding
    lateinit var weatherViewModel: WeatherViewModel
    lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()
        initUI()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navHostFragment)
        return navController.navigateUp()
    }

    private fun initViewModel() {
        val weatherRepository = WeatherRepository(this, WeatherDatabase(this))
        val viewModelProviderFactory = WeatherViewModelFactory(application, weatherRepository)
        weatherViewModel = ViewModelProvider(this, viewModelProviderFactory)[WeatherViewModel::class.java]
        weatherViewModel.isAppGPSEnabledLiveData.observe(this) { response ->
            if (response) {
                handleLocation()
            } else {
                resetCurrentCity()
            }
        }
    }

    private fun initUI() {
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.bottomNavigationView

        val navController = findNavController(R.id.navHostFragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_settings
            )
        )
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun handleLocation() {
        Utilities.requestLocationRuntimePermission(this) { allGranted, grantedList, deniedList ->
            if (allGranted) {
                getCurrentCity()
            } else {
                resetCurrentCity()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentCity() {
        // Get current location
        setSearchingForLocationTextViewVisible(true)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token
            override fun isCancellationRequested() = false
        }).addOnSuccessListener {location ->
            if (location != null) {
                // Get city from last location
                val geoCoder = Geocoder(this, Locale.getDefault())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geoCoder.getFromLocation(location.latitude,location.longitude,3) {addressList ->
                        if (addressList.size > 0) {
                            setCurrentCityFromName(addressList[0].adminArea)
                        } else {
                            resetCurrentCity()
                        }
                    }
                } else {
                    val addressList = geoCoder.getFromLocation(location.latitude,location.longitude,3)
                    if (addressList != null && addressList.size > 0) {
                        setCurrentCityFromName(addressList[0].adminArea)
                    } else {
                        resetCurrentCity()
                    }
                }
            }
        }.addOnFailureListener {
            resetCurrentCity()
        }
    }

    private fun setSearchingForLocationTextViewVisible(visible: Boolean) {
        runOnUiThread {
            binding.searchingForLocationTextView.visibility = if (visible) View.VISIBLE else View.GONE
        }
    }

    private fun setCurrentCityFromName(cityName: String) {
        setSearchingForLocationTextViewVisible(false)
        weatherViewModel.setCurrentCityFromName(cityName)
    }

    private fun resetCurrentCity() {
        setSearchingForLocationTextViewVisible(false)
        weatherViewModel.resetCurrentCity()
    }
}