package com.android.randomuser.common

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The Singleton class provides access to string resources within the application.
 */
@Singleton
class RUResourcesProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    /**
     * Retrieve a string resource from the app's resources associated with the provided resource ID.
     *
     * @param stringResId The resource ID of the string to retrieve.
     * @return The string value corresponding to the given resource ID.
     */
    fun getString(@StringRes stringResId: Int): String = context.getString(stringResId)

    /**
     * Retrieve a string array resource from the app's resources associated with the provided resource ID.
     *
     * @param stringArrayResId The resource ID of the string array to retrieve.
     * @return The string array value corresponding to the given resource ID.
     */
    fun getStringArray(@ArrayRes stringArrayResId: Int): Array<String> = context.resources.getStringArray(stringArrayResId)
}