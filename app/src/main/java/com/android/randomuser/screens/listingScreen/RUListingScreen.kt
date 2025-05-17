package com.android.randomuser.screens.listingScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.android.randomuser.R
import com.android.randomuser.ui.components.RUListView
import com.android.randomuser.ui.components.RULoadingIndicator
import com.android.randomuser.ui.components.RUOfflineBanner
import com.android.randomuser.ui.components.RUScaffold
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import com.android.randomuser.ui.components.RUBasicAlertDialog

@Composable
fun RUListingScreen(
    viewModel: RUListingViewModel = hiltViewModel(),
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

    if(uiState.isLoading) RULoadingIndicator()

    RUListingScreenUi(
        uiState = uiState,
        onFetchNextPage = {}
    )
}

@Composable
private fun RUListingScreenUi(
    listState: LazyGridState = rememberLazyGridState(),
    uiState: RUListingScreenUiState,
    onFetchNextPage: () -> Unit
) {
    RUScaffold(
        modifier = Modifier.fillMaxSize(),
        topBarTitle = stringResource(R.string.listing_screen),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primaryContainer),
        ) {
            RUOfflineBanner(isInternetConnected = uiState.isInternetAvailable)
            RUListView(
                gridState = listState,
                gridContent = { todo, index ->
                    RUTodo(
                        todoId = todo.userId,
                        id = todo.id,
                        title = todo.title,
                        completed = todo.completed,
                    )
                },
                onFetchNextPage = onFetchNextPage,
                list = uiState.todos,
                isLoading = uiState.isLoading,
            )
        }
    }
}

@Composable
private fun RUTodo(
    modifier: Modifier = Modifier,
    todoId: Int?,
    id: Int?,
    title: String?,
    completed: Boolean?
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Todo ID: ${todoId ?: "N/A"}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = title ?: "No Title",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (completed == true) "Status: Completed" else "Status: Not Completed",
                style = MaterialTheme.typography.bodyMedium,
                color = if (completed == true) Color.Green else Color.Red
            )
        }
    }
}