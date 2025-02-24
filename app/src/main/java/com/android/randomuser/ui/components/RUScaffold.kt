package com.android.randomuser.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RUScaffold(
    modifier: Modifier = Modifier,
    topBarTitle: String,
    goBack: @Composable () -> Unit = {},
    weatherDetails: RUWeatherDetails? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = topBarTitle,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )

                        if (weatherDetails != null) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    weatherDetails.temperature?.let {
                                        Text(
                                            text = "$it°C",
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                    }
                                    weatherDetails.weatherDescription?.let {
                                        Text(
                                            text = it,
                                            color = MaterialTheme.colorScheme.primaryContainer,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }

                                AsyncImage(
                                    model = weatherDetails.weatherIconUrl,
                                    contentDescription = "Weather Icon",
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                        }
                    }
                },
                navigationIcon = goBack,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { scaffoldPadding ->
        Box(modifier = Modifier.padding(scaffoldPadding)) {
            content(scaffoldPadding)
        }
    }
}

/**
 * The data class for the weather details
 */
data class RUWeatherDetails(
    val temperature: String? = null,
    val weatherDescription: String? = null,
    val weatherIconUrl: String? = null
)