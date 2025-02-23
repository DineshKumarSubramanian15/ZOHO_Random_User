package com.android.randomuser.common

import android.app.Application
import com.android.randomuser.common.database.RUDatabase
import dagger.hilt.android.HiltAndroidApp

/**
 * The Application class for the application. This class is annotated with
 * [HiltAndroidApp] to enable Hilt for dependency injection in the application.
 */
@HiltAndroidApp
class RUApplication : Application() {
    private var database: RUDatabase? = null

    companion object {
        private lateinit var instance: RUApplication

        /**
         * Retrieves the singleton instance of the [RUApplication].
         *
         * @return The application instance.
         */
        fun getInstance(): RUApplication = instance
    }

    /**
     * Called when the application is first created. Invokes the [initInstance] method to initialize
     * the singleton instance.
     */
    override fun onCreate() {
        super.onCreate()
        initInstance()
    }

    /**
     * Initializes the singleton instance for the current application.
     */
    private fun initInstance() {
        instance = this
        database = RUDatabase.getDatabase(this)
    }

    /**
     * To get the instance of database
     *
     * @return The database instance
     */
    fun getDatabase(): RUDatabase? = database
}