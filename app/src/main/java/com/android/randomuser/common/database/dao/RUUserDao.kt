package com.android.randomuser.common.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.android.randomuser.common.api.response.RUUser
import kotlinx.coroutines.flow.Flow

/**
 * The Data access object for the Random user.
 */
@Dao
interface RUUserDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertUsers(users: List<RUUser>)

    @Query("SELECT * FROM users")
    suspend fun getUsers(): List<RUUser>

    @Query("SELECT * FROM users")
    fun observeUsers(): Flow<List<RUUser>>

    @Query("DELETE FROM users")
    suspend fun clearUsers()

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmailAddress(email: String): RUUser?
}
