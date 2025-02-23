package com.android.randomuser.common.api

import com.android.randomuser.common.api.response.RUUsersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * The API service for the GoodAnime
 */
interface RUApiService {
    @GET("api/")
    suspend fun getUsers(
        @Query("results") results: Int = 25,
        @Query("page") page: Int = 1
    ): Response<RUUsersResponse>
}