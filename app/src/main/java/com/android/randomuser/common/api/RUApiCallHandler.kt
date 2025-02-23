package com.android.randomuser.common.api

import com.android.randomuser.R
import com.android.randomuser.common.connectivity.RUNetworkConnectivityStatusProvider
import com.android.randomuser.ui.components.RUUIText
import kotlinx.coroutines.flow.first
import retrofit2.Response
import java.net.UnknownHostException
import java.util.concurrent.CancellationException
import javax.inject.Inject

/**
 * The class to handle the api calls
 */
class RUApiCallHandlerImpl @Inject constructor(
    private val log: RULog,
    private val internetConnectivityStatusProvider: RUNetworkConnectivityStatusProvider,
) : RUApiCallHandler {
    override suspend fun <T> apiCall(func: suspend () -> Response<T>): Pair<RUApiResult<T?>, Response<T>?> {
        return try {
            if (internetConnectivityStatusProvider.internetStatus.first()) {
                val response = func()
                if (response.isSuccessful) {
                    Pair(RUApiResult.Success(response.body()), response)
                } else {
                    Pair(
                        RUApiResult.Failure(
                            exception = null,
                            errorCode = response.code(),
                            errorMessage = RUUIText.StringResource(R.string.something_went_wrong_please_try_again),
                        ), response
                    )
                }
            } else {
                Pair(
                    RUApiResult.Failure(
                        exception = UnknownHostException(),
                        errorCode = null,
                        errorMessage = RUUIText.StringResource(R.string.please_check_your_network_connection),
                    ),
                    null,
                )
            }
        } catch (exception: Exception) {
            if (exception is CancellationException) throw exception
            log.e(
                "RUApiCallHandlerImpl",
                "catch apiCall: " + exception.message.toString()
            )
            Pair(
                RUApiResult.Failure(
                    exception = exception,
                    errorCode = null,
                    errorMessage = RUUIText.StringResource(R.string.something_went_wrong_please_try_again),
                ),
                null
            )
        }
    }
}

/**
 * The interface that communicates with the api call handler
 */
interface RUApiCallHandler {
    /**
     * The function which takes another suspend function as a parameter, executes it and returns its result
     *
     * @param func The suspend function for the api call
     * @return The result
     */
    suspend fun <T> apiCall(func: suspend () -> Response<T>): Pair<RUApiResult<T?>, Response<T>?>
}