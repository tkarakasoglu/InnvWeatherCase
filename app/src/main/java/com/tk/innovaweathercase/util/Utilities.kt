package com.tk.innovaweathercase.util

import android.Manifest
import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.RequestCallback
import com.tk.innovaweathercase.R

class Utilities {
    companion object {
        fun isInternetAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }

        fun requestLocationRuntimePermission(fragmentActivity: FragmentActivity, callback: RequestCallback?) {
            PermissionX.init(fragmentActivity)
                .permissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .explainReasonBeforeRequest()
                .onExplainRequestReason { scope, deniedList ->
                    scope.showRequestReasonDialog(deniedList, "Core fundamental are based on these permissions", "OK", "Cancel")
                }
                .onForwardToSettings { scope, deniedList ->
                    scope.showForwardToSettingsDialog(deniedList, "You need to allow necessary permissions in Settings manually", "OK", "Cancel")
                }
                .request { allGranted, grantedList, deniedList ->
                    callback?.onResult(allGranted, grantedList, deniedList)
                }
        }

        fun checkLocationRuntimePermission(context: Context): Boolean {
            return PermissionX.isGranted(context, Manifest.permission.ACCESS_FINE_LOCATION)
        }

        fun isAppGPSEnabled(context: Context): Boolean {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

            // Initially GPS is on if not set
            if (!sharedPreferences.contains(context.getString(R.string.gps))) {
                val editor = sharedPreferences.edit()
                editor.putBoolean(context.getString(R.string.gps), true)
                editor.commit()
            }

            return sharedPreferences.getBoolean(context.getString(R.string.gps), true)
        }

        fun isSystemGPSEnabled(context: Context): Boolean {
            val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER
            )
        }
    }
}