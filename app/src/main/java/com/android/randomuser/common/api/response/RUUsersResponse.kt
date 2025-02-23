package com.android.randomuser.common.api.response

import androidx.room.Embedded
import com.google.gson.annotations.SerializedName
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * The response for the User API, which contains a list of users and the info for the response
 */
data class RUUsersResponse(
    @SerializedName("results") val results: List<RUUser>? = null,
    @SerializedName("info") val info: RUInfo? = null
)

/**
 * The info for the User API
 */
data class RUInfo(
    @SerializedName("seed") val seed: String? = null,
    @SerializedName("results") val results: Int? = null,
    @SerializedName("page") val page: Int? = null,
    @SerializedName("version") val version: String? = null
)

/**
 * The response for the User API
 */
@Entity(tableName = "users")
data class RUUser(
    @PrimaryKey
    @SerializedName("email") val email: String = "",
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("name")
    @Embedded
    val name: RUName? = null,
    @SerializedName("location")
    @Embedded
    val location: RULocation? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("cell") val cell: String? = null,
    @SerializedName("picture")
    @Embedded
    val picture: RUPicture? = null
)

/**
 * The location for the User API
 */
data class RULocation(
    @SerializedName("street")
    @Embedded
    val street: RUStreet? = null,
    @SerializedName("city") val city: String? = null,
    @SerializedName("state") val state: String? = null,
    @SerializedName("country") val country: String? = null,
    @SerializedName("postcode") val postcode: String? = null,
    @SerializedName("coordinates")
    @Embedded
    val coordinates: RUCoordinates? = null,
)

/**
 * The coordinates for the User API
 */
data class RUCoordinates(
    @SerializedName("latitude") val latitude: String? = null,
    @SerializedName("longitude") val longitude: String? = null
)

/**
 * The street for the User API
 */
data class RUStreet(
    @SerializedName("number") val number: Int? = null,
    @SerializedName("name") val name: String? = null
)

/**
 * The name for the User API
 */
data class RUName(
    @SerializedName("title") val title: String? = null,
    @SerializedName("first") val first: String? = null,
    @SerializedName("last") val last: String? = null
)

/**
 * The picture for the User API
 */
data class RUPicture(
    @SerializedName("large") val large: String? = null,
    @SerializedName("medium") val medium: String? = null,
    @SerializedName("thumbnail") val thumbnail: String? = null
)
