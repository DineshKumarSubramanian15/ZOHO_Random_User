package com.android.randomuser.common.api.repository

import com.android.randomuser.common.api.RUApiCallHandler
import com.android.randomuser.common.api.RUApiResult
import com.android.randomuser.common.api.remoteDataSource.RUTodoRemoteDataSource
import com.android.randomuser.common.api.response.RUTodo
import javax.inject.Inject

/**
 * The repository class for random users.
 */
class RUUsersRepoImpl @Inject constructor(
    private val apiCallHandler: RUApiCallHandler,
    private val todoRemoteDataSource: RUTodoRemoteDataSource,
) : RUUsersRepository {
    override suspend fun getTodos(): RUApiResult<List<RUTodo>?> =
        apiCallHandler.apiCall { todoRemoteDataSource.getTodos() }.first
}

/**
 * The interface for random users repository
 */
interface RUUsersRepository {
    /**
     * Get the users from the API
     */
    suspend fun getTodos(): RUApiResult<List<RUTodo>?>
}