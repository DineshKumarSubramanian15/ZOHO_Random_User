package com.android.randomuser.ui.components

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * The sealed interface that acts as a wrapper for dynamic and string resource
 */
sealed interface RUUIText {
    data class DynamicString(val value: String?) : RUUIText
    class StringResource(@StringRes val resId: Int, vararg val args: Any) : RUUIText

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value ?: EmptyString
            is StringResource -> stringResource(id = resId, formatArgs = args)
        }
    }

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value ?: EmptyString
            is StringResource -> context.getString(resId, args)
        }
    }
}