package com.tk.innovaweathercase.ui.settings

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.tk.innovaweathercase.R
import com.tk.innovaweathercase.ui.WeatherActivity
import com.tk.innovaweathercase.ui.WeatherViewModel

class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {
    private lateinit var weatherViewModel: WeatherViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        initViewModel()

        findPreference<Preference>(getString(R.string.gps))?.onPreferenceChangeListener = this
    }
    private fun initViewModel() {
        weatherViewModel = (activity as WeatherActivity).weatherViewModel
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
        when(preference.key) {
            getString(R.string.gps) -> {
                weatherViewModel.setAppGPSEnabled(newValue as Boolean)
            }
        }

        return true
    }

}