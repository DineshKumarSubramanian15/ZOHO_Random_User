package com.android.randomuser.common.api.remoteDataSource

import com.android.randomuser.common.api.RURetrofit
import com.android.randomuser.common.api.response.RUTodo
import retrofit2.Response
import javax.inject.Inject

/**
 * The remote data source class for Users.
 */
class RUTodoRemoteDataSourceImpl @Inject constructor(
    private val retrofit: RURetrofit,
) : RUTodoRemoteDataSource {
    override suspend fun getTodos(): Response<List<RUTodo>> =
        retrofit.apiService.getTodos()
}

/**
 * The interface for users remote data source
 */
interface RUTodoRemoteDataSource {
    /**
     * Get the users from the API
     *
     * @return The users response
     */
    suspend fun getTodos(): Response<List<RUTodo>>
}