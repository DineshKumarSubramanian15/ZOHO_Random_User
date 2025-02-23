package com.android.randomuser.common.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.randomuser.common.api.response.RUUser
import com.android.randomuser.common.database.dao.RUUserDao

/**
 * The Room database for the User API
 */
@Database(entities = [RUUser::class], version = 1, exportSchema = false)
abstract class RUDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: RUDatabase? = null

        /**
         * To get the database instance
         *
         * @param context Represent the app context
         */
        fun getDatabase(context: Context): RUDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RUDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

    /**
     * To get the user DAO
     */
    abstract fun userDao(): RUUserDao
}
