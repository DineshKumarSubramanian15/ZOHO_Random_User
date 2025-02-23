package com.android.randomuser.common.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * The Retrofit object for the Random User API.
 */
object RURetrofit {
    private const val API_BASE_URL = "https://randomuser.me/"
    private const val WEATHER_BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val WEATHER_API_KEY = "6c192de17df8f6f67814f29df7bfbf02"

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
    private val weatherRetrofit: Retrofit by lazy { createRetrofit(WEATHER_BASE_URL) }

    val apiService: RUApiService by lazy { apiRetrofit.create(RUApiService::class.java) }
    val weatherService: RUWeatherService by lazy { weatherRetrofit.create(RUWeatherService::class.java) }
}