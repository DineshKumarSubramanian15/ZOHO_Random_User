package com.android.randomuser.common.api

import com.android.randomuser.ui.components.RUUIText

/**
 * The class that denotes the api result type and holds the fetched data
 */
sealed class RUApiResult<out R> {

    /**
     * The class that denotes the successful result
     *
     * @param data The response data of type [T]
     */
    data class Success<out T>(val data: T) : RUApiResult<T>()

    /**
     * The class representing the failed result with an error and exception
     *
     * @param exception    The exception occurred
     * @param errorCode    The error code
     * @param errorMessage The error message
     */
    data class Failure(
        val exception: Exception? = null,
        val errorCode: Int?,
        val errorMessage: RUUIText?,
    ) : RUApiResult<Nothing>()
}

/**
 * Executes the given action if this result is a Success and has data.
 *
 * @param action The lambda function to be executed with the retrieved data.
 * @return The original DLResultNew instance.
 */
inline fun <T> RUApiResult<T>.onSuccess(action: (T?) -> Unit): RUApiResult<T> {
    if (this is RUApiResult.Success) {
        action(data)
    }
    return this
}

/**
 * Define a utility function to handle the data layer result in a consistent way
 *
 * @param onSuccess The success block of the [RUApiResult]
 * @param onFailure The failure block of the [RUApiResult]
 */
inline fun <T> RUApiResult<T>.handleDataLayerResult(
    onSuccess: (T) -> Unit,
    onFailure: (exception: Exception?, errorCode: Int?, errorMessage: RUUIText?) -> Unit
) {
    when (this) {
        is RUApiResult.Success -> onSuccess(data)
        is RUApiResult.Failure -> onFailure(exception, errorCode, errorMessage)
    }
}