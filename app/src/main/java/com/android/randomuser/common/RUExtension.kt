package com.android.randomuser.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.android.randomuser.common.api.RULog

private const val TAG = "RUExtension"

/**
 * Launches an intent to open the application settings for the current package.
 * This allows the user to manage app settings, permissions, and other details.
 */
fun Context.launchApplicationSettings() {
    try {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:$packageName")
        startActivity(intent)
    } catch (e: Exception) {
        RULog.e(TAG, "ApplicationSettings Exception " + e.localizedMessage)
    }
}

/**
 * Launches an intent to open the location settings for the device.
 * This allows the user to manage location settings, permissions, and other details.
 */
fun Context.launchLocationSettings() {
    try {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    } catch (e: Exception) {
        RULog.e(TAG, "LocationSettings Exception " + e.localizedMessage)
    }
}