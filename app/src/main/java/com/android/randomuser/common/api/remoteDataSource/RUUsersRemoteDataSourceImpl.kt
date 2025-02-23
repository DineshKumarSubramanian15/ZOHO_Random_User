package com.android.randomuser.common.api.remoteDataSource

import com.android.randomuser.common.api.RURetrofit
import com.android.randomuser.common.api.response.RUUsersResponse
import com.android.randomuser.common.api.response.RUWeatherResponse
import retrofit2.Response
import javax.inject.Inject

/**
 * The remote data source class for Users.
 */
class RUUsersRemoteDataSourceImpl @Inject constructor(
    private val retrofit: RURetrofit,
) : RUUsersRemoteDataSource {
    override suspend fun getUsers(): Response<RUUsersResponse> =
        retrofit.apiService.getUsers()

    override suspend fun getUsersNextPage(page: Int): Response<RUUsersResponse> =
        retrofit.apiService.getUsers(page = page)

    override suspend fun getWeather(
        lat: Double,
        lon: Double,
        apiKey: String
    ): Response<RUWeatherResponse> =
        retrofit.weatherService.getWeather(
            lat = lat,
            lon = lon,
            apiKey = apiKey
        )
}

/**
 * The interface for users remote data source
 */
interface RUUsersRemoteDataSource {
    /**
     * Get the users from the API
     *
     * @return The users response
     */
    suspend fun getUsers(): Response<RUUsersResponse>

    /**
     * Get the users from the API
     *
     * @param page The page number
     * @return The users response
     */
    suspend fun getUsersNextPage(page: Int): Response<RUUsersResponse>

    /**
     * Get the weather from the API
     *
     * @param lat The latitude of the location
     * @param lon The longitude of the location
     * @param apiKey The API key
     * @return The weather response
     */
    suspend fun getWeather(
        lat: Double,
        lon: Double,
        apiKey: String
    ): Response<RUWeatherResponse>
}