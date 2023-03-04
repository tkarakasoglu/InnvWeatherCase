package com.tk.innovaweathercase.ui.settings

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.tk.innovaweathercase.R
import com.tk.innovaweathercase.ui.WeatherActivity
import com.tk.innovaweathercase.ui.WeatherViewModel
import com.tk.innovaweathercase.util.Utilities

class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var weatherActivity: WeatherActivity

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        initViewModel()

        findPreference<Preference>(getString(R.string.gps))?.onPreferenceChangeListener = this
    }

    private fun initViewModel() {
        weatherActivity = activity as WeatherActivity
        weatherViewModel = weatherActivity.weatherViewModel
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
        when(preference.key) {
            getString(R.string.gps) -> {
                if (newValue as Boolean) {
                    if (!weatherActivity.initLocationPermission()) {
                        return false
                    }
                } else {
                    // If App GPS turned off call getCityFavoritesList and refresh home fragment
                    weatherViewModel.resetCurrentCity()
                }
            }
        }

        return true
    }

}