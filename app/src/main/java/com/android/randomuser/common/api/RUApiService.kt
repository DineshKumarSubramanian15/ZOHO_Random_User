package com.android.randomuser.common.api

import com.android.randomuser.common.api.response.RUTodo
import retrofit2.Response
import retrofit2.http.GET

/**
 * The API service for the GoodAnime
 */
interface RUApiService {
    @GET("todos")
    suspend fun getTodos(): Response<List<RUTodo>>
}