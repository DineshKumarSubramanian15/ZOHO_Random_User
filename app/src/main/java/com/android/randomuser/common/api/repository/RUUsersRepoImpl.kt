package com.android.randomuser.common.api.repository

import com.android.randomuser.common.api.RUApiCallHandler
import com.android.randomuser.common.api.RUApiResult
import com.android.randomuser.common.api.RURetrofit.WEATHER_API_KEY
import com.android.randomuser.common.api.localDataSource.RUUsersLocalDataSource
import com.android.randomuser.common.api.onSuccess
import com.android.randomuser.common.api.remoteDataSource.RUUsersRemoteDataSource
import com.android.randomuser.common.api.response.RUUser
import com.android.randomuser.common.api.response.RUUsersResponse
import com.android.randomuser.common.api.response.RUWeatherResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * The repository class for random users.
 */
class RUUsersRepoImpl @Inject constructor(
    private val apiCallHandler: RUApiCallHandler,
    private val usersRemoteDataSource: RUUsersRemoteDataSource,
    private val localDataSource: RUUsersLocalDataSource
) : RUUsersRepository {
    override suspend fun getUsers(): RUApiResult<RUUsersResponse?> {
        val result = apiCallHandler.apiCall {
            usersRemoteDataSource.getUsers()
        }.first
        result.onSuccess {
            with(localDataSource) {
                clearUsers()
                insertUsers(it?.results ?: emptyList())
            }
        }
        return result
    }

    override suspend fun getUsersNextPage(page: Int): RUApiResult<RUUsersResponse?> {
        val result = apiCallHandler.apiCall {
            usersRemoteDataSource.getUsersNextPage(page)
        }.first
        result.onSuccess {
            with(localDataSource) {
                insertUsers(it?.results ?: emptyList())
            }
        }
        return result
    }

    override fun observeUsers(): Flow<List<RUUser>> = localDataSource.observeUsers()

    override suspend fun getUserByEmailAddress(email: String): RUUser? =
        localDataSource.getUserByEmailAddress(email)

    override suspend fun getWeather(
        lat: Double,
        lon: Double
    ): RUApiResult<RUWeatherResponse?> = apiCallHandler.apiCall {
        usersRemoteDataSource.getWeather(
            lat = lat,
            lon = lon,
            apiKey = WEATHER_API_KEY
        )
    }.first
}

/**
 * The interface for random users repository
 */
interface RUUsersRepository {
    /**
     * Get the users from the API
     */
    suspend fun getUsers(): RUApiResult<RUUsersResponse?>

    /**
     * Get the users from the API
     *
     * @param page The page number
     * @return The users response
     */
    suspend fun getUsersNextPage(page: Int): RUApiResult<RUUsersResponse?>

    /**
     * Observes the users as flow of list
     *
     * @return The users as flow
     */
    fun observeUsers(): Flow<List<RUUser>>

    /**
     * To get user by email address
     *
     * @param email The email address of the user
     * @return The user with the email address
     */
    suspend fun getUserByEmailAddress(email: String): RUUser?

    /**
     * To get the weather from the API
     *
     * @param lat The latitude of the location
     * @param lon The longitude of the location
     * @return The weather response
     */
    suspend fun getWeather(
        lat: Double,
        lon: Double
    ): RUApiResult<RUWeatherResponse?>
}