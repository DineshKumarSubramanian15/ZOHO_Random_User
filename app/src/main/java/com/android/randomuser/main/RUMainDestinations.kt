package com.android.randomuser.main

/**
 * The class that representing the destinations from the main navigation
 */
sealed class RUMainDestinations(val route: String) {
    object ListingScreen : RUMainDestinations(route = "ListingScreen")
}