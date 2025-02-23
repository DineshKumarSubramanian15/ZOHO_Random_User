package com.android.randomuser.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.randomuser.screens.detailsScreen.RUDetailsScreen
import com.android.randomuser.screens.listingScreen.RUListingScreen
import com.android.randomuser.ui.theme.RandomUserTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RUMainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RandomUserTheme {
                val navController = rememberNavController()
                MainNavHost(navController)
            }
        }
    }
}

@Composable
private fun MainNavHost(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = RUMainDestinations.ListingScreen.route,
        builder = {
            composable(
                route = RUMainDestinations.ListingScreen.route,
                content = { RUListingScreen(navController = navController) },
            )
            composable(
                route = RUMainDestinations.DetailsScreen.route.plus("/{${RUNavigationArguments.EMAIL}}"),
                content = { RUDetailsScreen(navController = navController) },
                arguments = listOf(
                    navArgument(RUNavigationArguments.EMAIL) {
                        type = NavType.StringType
                        nullable = false
                    }
                )
            )
        }
    )
}