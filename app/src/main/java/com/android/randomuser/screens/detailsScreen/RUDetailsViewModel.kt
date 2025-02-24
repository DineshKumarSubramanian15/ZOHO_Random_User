package com.android.randomuser.screens.detailsScreen

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.randomuser.R
import com.android.randomuser.common.api.handleDataLayerResult
import com.android.randomuser.common.api.repository.RUUsersRepository
import com.android.randomuser.common.connectivity.RUNetworkConnectivityStatusProvider
import com.android.randomuser.main.RUNavigationArguments
import com.android.randomuser.ui.components.RUUIText
import com.android.randomuser.ui.components.RUWeatherDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The view model for the details screen. It handles the business logic to communicate with the model for the
 * random user and provides the data to the observing UI component.
 */
@HiltViewModel
class RUDetailsViewModel @Inject constructor(
    private val usersRepo: RUUsersRepository,
    internetConnectivityStatusProvider: RUNetworkConnectivityStatusProvider,
    savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val TAG = RUDetailsViewModel::class.java.simpleName
    val uiState: MutableStateFlow<RUDetailsScreenUiState> =
        MutableStateFlow(RUDetailsScreenUiState())

    init {
        viewModelScope.launch {
            internetConnectivityStatusProvider.internetStatus.collect { isConnected ->
                uiState.value = uiState.value.copy(isInternetAvailable = isConnected)
            }
        }
        getUserDetails(savedStateHandle.get<String>(RUNavigationArguments.EMAIL))
    }

    /**
     * Get the user details
     */
    private fun getUserDetails(email: String?) {
        viewModelScope.launch {
            if (email == null) {
                uiState.value = uiState.value.copy(error = RUUIText.StringResource(R.string.unable_to_find_the_user))
                return@launch
            }
            val userDetails = usersRepo.getUserByEmailAddress(email)
            fetchWeatherData(
                latitude = userDetails?.location?.coordinates?.latitude,
                longitude = userDetails?.location?.coordinates?.longitude
            )
            uiState.update {
                it.copy(
                    userDetails = RUUserDetails(
                        name = "${userDetails?.name?.title} ${userDetails?.name?.first} ${userDetails?.name?.last}",
                        gender = userDetails?.gender,
                        email = userDetails?.email,
                        profilePic = userDetails?.picture?.large,
                        phone = userDetails?.phone,
                        cell = userDetails?.cell
                    )
                )
            }
        }
    }

    /**
     * Fetch the weather data based on the given latitude and longitude.
     *
     * @param latitude The latitude of the location
     * @param longitude The longitude of the location
     */
    private fun fetchWeatherData(latitude: String?, longitude: String?) {
        if (latitude == null || longitude == null) {
            return
        }
        viewModelScope.launch {
            usersRepo.getWeather(latitude.toDouble(), longitude.toDouble()).handleDataLayerResult(
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
     * Dismiss the error message.
     */
    fun dismissError() {
        uiState.update { it.copy(error = null) }
    }
}

/**
 * The UI state for the listing screen
 */
data class RUDetailsScreenUiState(
    val isLoading: Boolean = false,
    val userDetails: RUUserDetails = RUUserDetails(),
    val error: RUUIText? = null,
    val isInternetAvailable: Boolean = true,
    val weatherDetails: RUWeatherDetails? = null
)

/**
 * The data class for the user details to be displayed
 */
data class RUUserDetails(
    val name: String? = null,
    val gender: String? = null,
    val email: String? = null,
    val profilePic: String? = null,
    val phone: String? = null,
    val cell: String? = null,
)