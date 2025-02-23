package com.android.randomuser.screens.listingScreen

import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.randomuser.common.api.handleDataLayerResult
import com.android.randomuser.common.api.response.RUUser
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
import androidx.core.app.ActivityCompat
import com.android.randomuser.common.api.RULog
import com.android.randomuser.common.di.RUDefaultDispatcher
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
        requestLocationPermission()
    }

    /**
     * Handle the permission request result. If the permission is granted, then get the location.
     */
    private fun requestLocationPermission() {
        if (
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
        RULog.d(TAG, "Getting location")
        uiState.update { it.copy(launchLocationPermission = false) }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.getFusedLocationProviderClient(context).lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val latitude = it.latitude
                    val longitude = it.longitude
                    fetchWeatherData(latitude, longitude)
                } ?: RULog.d(TAG, "Location is null")
            }
        }
    }

    /**
     * Fetch the weather data based on the given latitude and longitude.
     *
     * @param latitude The latitude of the location
     * @param longitude The longitude of the location
     */
    private fun fetchWeatherData(latitude: Double, longitude: Double) {
        RULog.d(TAG, "Fetching weather data")
        viewModelScope.launch {
            usersRepo.getWeather(latitude, longitude).handleDataLayerResult(
                onSuccess = {
                    RULog.d(TAG, "Weather data: $it")
                },
                onFailure = { _, _, errorMessage ->
                    RULog.d(TAG, "Error: $errorMessage")
                    uiState.update { it.copy(error = errorMessage, isLoading = false) }
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
     * Get the random user data. This method will convert the random user data to the UI model.
     *
     * @param user The random user data
     * @return The UI model for the random user
     */
    private fun getRandomUserData(user: List<RUUser>?): List<RURandomUserItem?> {
        return user?.map {
            RURandomUserItem(
                name = "${it.name?.title} ${it.name?.first} ${it.name?.last}",
                gender = it.gender,
                email = it.email,
                profilePic = it.picture?.large
            )
        } ?: emptyList()
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
    val launchLocationPermission: Boolean = false
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