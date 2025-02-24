package com.android.randomuser.screens.listingScreen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.android.randomuser.R
import com.android.randomuser.main.RUMainDestinations
import com.android.randomuser.ui.components.RUListView
import com.android.randomuser.ui.components.RULoadingIndicator
import com.android.randomuser.ui.components.RUOfflineBanner
import com.android.randomuser.ui.components.RUOutlinedTextField
import com.android.randomuser.ui.components.RUOutlinedTextFieldState
import com.android.randomuser.ui.components.RUProfilePicture
import com.android.randomuser.ui.components.RUScaffold
import android.Manifest
import android.content.IntentFilter
import android.location.LocationManager
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.android.randomuser.common.RUGpsReceiver
import com.android.randomuser.common.launchApplicationSettings
import com.android.randomuser.common.launchLocationSettings
import com.android.randomuser.ui.components.RUBasicAlertDialog
import com.android.randomuser.ui.components.RUText

@Composable
fun RUListingScreen(
    viewModel: RUListingViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val gpsReceiver = remember { RUGpsReceiver { isEnabled ->
        if (isEnabled) {
            viewModel.getLocation()
        }
    } }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsResult ->
        val fineLocationGranted = permissionsResult[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissionsResult[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineLocationGranted || coarseLocationGranted) {
            viewModel.getLocation()
        } else {
            context.launchApplicationSettings()
        }
    }

    DisposableEffect(Unit) {
        val intentFilter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        context.registerReceiver(gpsReceiver, intentFilter)

        onDispose {
            context.unregisterReceiver(gpsReceiver)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.requestLocationPermission()
    }

    LaunchedEffect(uiState.launchLocationPermission) {
        if (uiState.launchLocationPermission) {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    if (uiState.gpsAlert) {
        RUBasicAlertDialog(
            message = stringResource(R.string.gps_alert_message),
            confirmButtonText = stringResource(R.string.ok),
            onConfirmButtonClicked = {
                viewModel.gpsAlert(false)
                context.launchLocationSettings()
             },
            dismissButtonText = stringResource(R.string.cancel),
            onDismissed = { viewModel.gpsAlert(false) }
        )
    }

    if (uiState.locationAlert) {
        RUBasicAlertDialog(
            message = stringResource(R.string.location_alert_message),
            confirmButtonText = stringResource(R.string.ok),
            onConfirmButtonClicked = {
                viewModel.locationAlert(false)
                context.launchApplicationSettings()
            },
            dismissButtonText = stringResource(R.string.cancel),
            onDismissed = { viewModel.locationAlert(false) }
        )
    }

    uiState.error?.asString()?.let {
        RUBasicAlertDialog(
            message = it,
            confirmButtonText = stringResource(R.string.ok),
            onConfirmButtonClicked = viewModel::dismissError
        )
    }

    if(uiState.isLoading) RULoadingIndicator()

    RUListingScreenUi(
        uiState = uiState,
        onClick = {
            navController.navigate(RUMainDestinations.DetailsScreen.route.plus("/${it}"))
        },
        onFetchNextPage = viewModel::getUsersNextPage,
        searchTextFieldState = viewModel.searchTextFieldState
    )
}

@Composable
private fun RUListingScreenUi(
    listState: LazyGridState = rememberLazyGridState(),
    uiState: RUListingScreenUiState,
    onClick: (String?) -> Unit,
    onFetchNextPage: () -> Unit,
    searchTextFieldState: RUOutlinedTextFieldState
) {
    RUScaffold(
        modifier = Modifier.fillMaxSize(),
        topBarTitle = stringResource(R.string.listing_screen),
        weatherDetails = uiState.weatherDetails,
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primaryContainer),
        ) {
            RUOfflineBanner(isInternetConnected = uiState.isInternetAvailable)
            RUOutlinedTextField(
                state = searchTextFieldState,
                placeholder = stringResource(R.string.search),
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )
            RUListView(
                gridState = listState,
                gridContent = { user, index ->
                    RUUser(
                        index = index,
                        name = user.name,
                        gender = user.gender,
                        email = user.email,
                        profilePic = user.profilePic,
                        onClick = onClick
                    )
                },
                onFetchNextPage = onFetchNextPage,
                list = uiState.randomUser,
                isLoading = uiState.isLoading,
            )
        }
    }
}

@Composable
private fun RUUser(
    modifier: Modifier = Modifier,
    index: Int,
    name: String?,
    gender: String?,
    email: String?,
    profilePic: String?,
    onClick: (String?) -> Unit
) {
    Column(
        modifier = modifier
            .border(
                1.dp,
                MaterialTheme.colorScheme.onSecondaryContainer,
                MaterialTheme.shapes.medium
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().clickable(onClick = { onClick(email) }),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RUProfilePicture(
                profilePic = profilePic,
                name = name,
            )
        }
        if (name != null) {
            RUText(
                text = name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}