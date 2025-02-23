package com.android.randomuser.common.api.localDataSource

import com.android.randomuser.common.api.response.RUUser
import com.android.randomuser.common.database.dao.RUUserDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * The local data source for Users. This class is responsible for handling the local data source for users.
 */
class RUUsersLocalDataSourceImpl @Inject constructor(
    private val userDao: RUUserDao
) : RUUsersLocalDataSource {
    override suspend fun insertUsers(users: List<RUUser>) {
        userDao.insertUsers(users)
    }

    override suspend fun getUsers(): List<RUUser> = userDao.getUsers()

    override fun observeUsers(): Flow<List<RUUser>> = userDao.observeUsers()

    override suspend fun clearUsers() {
        userDao.clearUsers()
    }

    override suspend fun getUserByEmailAddress(email: String): RUUser? =
        userDao.getUserByEmailAddress(email)
}

/**
 * The interface to communicate with the users local data source
 */
interface RUUsersLocalDataSource {

    /**
     * To insert users
     *
     * @param users The list of users to insert
     */
    suspend fun insertUsers(users: List<RUUser>)

    /**
     * To get users
     *
     * @return The list of users
     */
    suspend fun getUsers(): List<RUUser>

    /**
     * Observes users as flow of list
     *
     * @return The users as flow
     */
    fun observeUsers(): Flow<List<RUUser>>

    /**
     * To clear the users
     */
    suspend fun clearUsers()

    /**
     * To get user by email address
     *
     * @param email The email address of the user
     * @return The user with the email address
     */
    suspend fun getUserByEmailAddress(email: String): RUUser?
}