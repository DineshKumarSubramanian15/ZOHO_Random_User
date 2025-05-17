package com.android.randomuser.common.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * The Retrofit object for the Random User API.
 */
object RURetrofit {
    private const val API_BASE_URL = "https://jsonplaceholder.typicode.com/"

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    private fun createRetrofit(baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    private val apiRetrofit: Retrofit by lazy { createRetrofit(API_BASE_URL) }

    val apiService: RUApiService by lazy { apiRetrofit.create(RUApiService::class.java) }
}