package com.android.randomuser.screens.listingScreen

import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.randomuser.common.api.handleDataLayerResult
import com.android.randomuser.common.api.repository.RUUsersRepository
import com.android.randomuser.common.connectivity.RUNetworkConnectivityStatusProvider
import com.android.randomuser.ui.components.RUOutlinedTextFieldState
import com.android.randomuser.ui.components.RUUIText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.widget.Toast
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.core.app.ActivityCompat
import com.android.randomuser.R
import com.android.randomuser.common.RUResourcesProvider
import com.android.randomuser.common.di.RUDefaultDispatcher
import com.android.randomuser.ui.components.RUWeatherDetails
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

/**
 * The view model for the listing screen. It handles the business logic to communicate with the model for the
 * random user and provides the data to the observing UI component.
 */
@HiltViewModel
class RUListingViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val usersRepo: RUUsersRepository,
    internetConnectivityStatusProvider: RUNetworkConnectivityStatusProvider,
    @RUDefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val resourceProvider: RUResourcesProvider
) : ViewModel() {
    private val TAG = RUListingViewModel::class.java.simpleName
    val uiState: MutableStateFlow<RUListingScreenUiState> =
        MutableStateFlow(RUListingScreenUiState())
    private var pageNumber = 1
    private var allUsers: List<RURandomUserItem?> = emptyList()
    val searchTextFieldState = RUOutlinedTextFieldState(
        onValueUpdated = ::searchUsers
    )

    init {
        viewModelScope.launch {
            usersRepo.observeUsers()
                .distinctUntilChanged()
                .map { users ->
                    users.map {
                        RURandomUserItem(
                            name = "${it.name?.title} ${it.name?.first} ${it.name?.last}",
                            gender = it.gender,
                            email = it.email,
                            profilePic = it.picture?.large
                        )
                    }
                }
                .flowOn(defaultDispatcher)
                .collect { users ->
                    allUsers = users
                    uiState.update { it.copy(randomUser = allUsers)
                }
            }
        }
        viewModelScope.launch {
            internetConnectivityStatusProvider.internetStatus.collect { isConnected ->
                uiState.update { it.copy(isInternetAvailable = isConnected) }
            }
        }
        getUsers()
    }

    /**
     * Handle the permission request result. If the permission is granted, then get the location.
     */
    fun requestLocationPermission() {
        if (
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED
        ) {
            uiState.update { it.copy(launchLocationPermission = true) }
        } else {
            getLocation()
        }
    }

    /**
     * Get the location of the user.
     */
    fun getLocation() {
        uiState.update { it.copy(launchLocationPermission = false) }
        if (!isGPSEnabled()) {
            gpsAlert(true)
            return
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.getFusedLocationProviderClient(context).lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val latitude = it.latitude
                    val longitude = it.longitude
                    fetchWeatherData(latitude, longitude)
                } ?: Toast.makeText(
                    context,
                    resourceProvider.getString(R.string.unable_to_get_location),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Show the location alert dialog.
     *
     * @param show True if the location alert dialog should be shown, false otherwise
     */
    fun locationAlert(show: Boolean) {
        uiState.update { it.copy(locationAlert = show) }
    }

    /**
     * Show the GPS alert dialog.
     *
     * @param show True if the GPS alert dialog should be shown, false otherwise
     */
    fun gpsAlert(show: Boolean) {
        uiState.update { it.copy(gpsAlert = show) }
    }

    /**
     * Check if the GPS is enabled.
     *
     * @return True if the GPS is enabled, false otherwise
     */
    private fun isGPSEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /**
     * Fetch the weather data based on the given latitude and longitude.
     *
     * @param latitude The latitude of the location
     * @param longitude The longitude of the location
     */
    private fun fetchWeatherData(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            usersRepo.getWeather(latitude, longitude).handleDataLayerResult(
                onSuccess = { result ->
                    uiState.update {
                        it.copy(
                            weatherDetails = RUWeatherDetails(
                                temperature = result?.main?.temp?.toString(),
                                weatherDescription = result?.weather?.get(0)?.description?.capitalize(Locale.current),
                                weatherIconUrl = "https://openweathermap.org/img/wn/${result?.weather?.get(0)?.icon}.png"
                            )
                        )
                    }
                },
                onFailure = { _, _, errorMessage ->
                    uiState.update { it.copy(error = errorMessage, isLoading = false, weatherDetails = null) }
                }
            )
        }
    }

    /**
     * Search the users based on the given value.
     *
     * @param searchText The value to search
     */
    private fun searchUsers(searchText: String?) {
        uiState.update {
            it.copy(
                randomUser = if (searchText?.isNotBlank() == true) {
                    allUsers.filter { item ->
                        item?.name?.contains(searchText, true) == true ||
                                item?.email?.contains(searchText, true) ?: false
                    }
                } else {
                    allUsers
                }
            )
        }
    }

    /**
     * Get the random users from the API.
     */
    private fun getUsers() {
        uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            usersRepo.getUsers().handleDataLayerResult(
                onSuccess = { uiState.update { it.copy(isLoading = false) } },
                onFailure = { _, _, errorMessage ->
                    uiState.update { it.copy(error = errorMessage, isLoading = false) }
                }
            )
        }
    }

    /**
     * Get the next page of the random users.
     */
    fun getUsersNextPage() {
        uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            usersRepo.getUsersNextPage(pageNumber).handleDataLayerResult(
                onSuccess = {
                    uiState.update { it.copy(isLoading = false) }
                    pageNumber++
                },
                onFailure = { _, _, errorMessage ->
                    uiState.update { it.copy(error = errorMessage, isLoading = false) }
                }
            )
        }
    }

    /**
     * Dismiss the error message.
     */
    fun dismissError() {
        uiState.update { it.copy(error = null) }
    }
}

/**
 * The UI state for the listing screen
 */
data class RUListingScreenUiState(
    val isLoading: Boolean = false,
    val randomUser: List<RURandomUserItem?> = emptyList(),
    val error: RUUIText? = null,
    val isInternetAvailable: Boolean = true,
    val launchLocationPermission: Boolean = false,
    val weatherDetails: RUWeatherDetails? = null,
    val launchLocationSettings: Boolean = false,
    val gpsAlert: Boolean = false,
    val locationAlert: Boolean = false
)

/**
 * The data class for the random user item
 */
data class RURandomUserItem(
    val name: String? = null,
    val gender: String? = null,
    val email: String? = null,
    val profilePic: String? = null
)