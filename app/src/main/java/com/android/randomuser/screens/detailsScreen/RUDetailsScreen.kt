package com.android.randomuser.screens.detailsScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.android.randomuser.R
import com.android.randomuser.ui.components.RUBasicAlertDialog
import com.android.randomuser.ui.components.RUOfflineBanner
import com.android.randomuser.ui.components.RUProfilePicture
import com.android.randomuser.ui.components.RUScaffold

@Composable
fun RUDetailsScreen(
    viewModel: RUDetailsViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()

    uiState.error?.asString()?.let {
        RUBasicAlertDialog(
            message = it,
            confirmButtonText = stringResource(R.string.ok),
            onConfirmButtonClicked = viewModel::dismissError
        )
    }

    RUDetailsUI(
        uiState = uiState,
        goBack = { navController.popBackStack() }
    )
}

@Composable
private fun RUDetailsUI(
    uiState: RUDetailsScreenUiState,
    goBack: () -> Unit,
    scrollState: ScrollState = rememberScrollState()
) {
    RUScaffold(
        modifier = Modifier.fillMaxSize(),
        topBarTitle = stringResource(R.string.details_screen),
        goBack = {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_left),
                contentDescription = "Back",
                modifier = Modifier.clickable(onClick = goBack),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        },
        weatherDetails = uiState.weatherDetails,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .verticalScroll(scrollState),
        ) {
            RUOfflineBanner(isInternetConnected = uiState.isInternetAvailable)
            RUProfilePicture(
                profilePic = uiState.userDetails.profilePic,
                name = uiState.userDetails.name,
                modifier = Modifier
                    .height(200.dp)
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp)
            )
            uiState.userDetails.name?.let {
                RUDetailsItem(
                    title = stringResource(R.string.name),
                    value = it
                )
            }
            uiState.userDetails.email?.let {
                RUDetailsItem(
                    title = stringResource(R.string.email),
                    value = it
                )
            }
            uiState.userDetails.phone?.let {
                RUDetailsItem(
                    title = stringResource(R.string.phone),
                    value = it
                )
            }
            uiState.userDetails.cell?.let {
                RUDetailsItem(
                    title = stringResource(R.string.phone),
                    value = it
                )
            }
            uiState.userDetails.gender?.let {
                RUDetailsItem(
                    title = stringResource(R.string.gender),
                    value = it
                )
            }
        }
    }
}

@Composable
fun RUDetailsItem(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
) {
    Column (
        modifier = modifier
            .heightIn(56.dp)
            .fillMaxWidth()
            .padding(8.dp)
            .clip(MaterialTheme.shapes.medium)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                shape = MaterialTheme.shapes.medium
            )
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = value,
                modifier = Modifier.weight(2f),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            )
        }
    }
}