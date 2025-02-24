package com.android.randomuser.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager

/**
 * A broadcast receiver to listen to the GPS state changes.
 *
 * @param onGPSStateChanged The callback to be invoked when the GPS state changes.
 */
class RUGpsReceiver(private val onGPSStateChanged: (Boolean) -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == LocationManager.PROVIDERS_CHANGED_ACTION) {
            val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            onGPSStateChanged(isEnabled)
        }
    }
}
