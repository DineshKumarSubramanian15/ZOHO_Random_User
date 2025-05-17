package com.android.randomuser.screens.listingScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.randomuser.common.api.handleDataLayerResult
import com.android.randomuser.common.api.repository.RUUsersRepository
import com.android.randomuser.common.connectivity.RUNetworkConnectivityStatusProvider
import com.android.randomuser.ui.components.RUUIText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The view model for the listing screen. It handles the business logic to communicate with the model for the
 * random user and provides the data to the observing UI component.
 */
@HiltViewModel
class RUListingViewModel @Inject constructor(
    private val usersRepo: RUUsersRepository,
    internetConnectivityStatusProvider: RUNetworkConnectivityStatusProvider
) : ViewModel() {
    private val TAG = RUListingViewModel::class.java.simpleName
    val uiState: MutableStateFlow<RUListingScreenUiState> =
        MutableStateFlow(RUListingScreenUiState())
    private val todos: MutableList<RUTodoItem> = mutableListOf()

    init {
        viewModelScope.launch {
            internetConnectivityStatusProvider.internetStatus.collect { isConnected ->
                uiState.update { it.copy(isInternetAvailable = isConnected) }
            }
        }
        getTodos()
    }

    /**
     * Get the random todos from the API.
     */
    private fun getTodos() {
        uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            usersRepo.getTodos().handleDataLayerResult(
                onSuccess = {
                    it?.map {
                        todos.add(
                            RUTodoItem(
                                userId = it.userId,
                                id = it.id,
                                title = it.title,
                                completed = it.completed
                            )
                        )
                    }
                    uiState.update { it.copy(todos = todos, isLoading = false) }
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
    val todos: List<RUTodoItem?> = emptyList(),
    val error: RUUIText? = null,
    val isInternetAvailable: Boolean = true,
)

/**
 * The data class for the random user item
 */
data class RUTodoItem(
    val userId: Int? = null,
    val id: Int? = null,
    val title: String? = null,
    val completed: Boolean? = null
)