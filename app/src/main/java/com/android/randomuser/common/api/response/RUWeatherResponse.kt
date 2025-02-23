package com.android.randomuser.common.api.response

import com.google.gson.annotations.SerializedName

/**
 * The response for the weather API.
 */
data class RUWeatherResponse(
    @SerializedName("main") val main: RUWeatherMain? = null,
    @SerializedName("weather") val weather: List<RUWeather>? = null,
    @SerializedName("wind") val wind: RUWind? = null
)

/**
 * The main data for the weather API.
 */
data class RUWeatherMain(
    @SerializedName("temp") val temp: Double? = null,
    @SerializedName("feels_like") val feelsLike: Double? = null,
    @SerializedName("temp_min") val tempMin: Double? = null,
    @SerializedName("temp_max") val tempMax: Double? = null,
    @SerializedName("pressure") val pressure: Int? = null,
    @SerializedName("humidity") val humidity: Int? = null
)

/**
 * The wind data for the weather API.
 */
data class RUWind(
    @SerializedName("speed") val speed: Double? = null,
    @SerializedName("deg") val deg: Int? = null
)

/**
 * The weather data for the weather API.
 */
data class RUWeather(
    @SerializedName("description") val description: String? = null,
    @SerializedName("icon") val icon: String? = null
)

