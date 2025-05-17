package com.android.randomuser.common.api.response

import com.google.gson.annotations.SerializedName

data class RUTodo(
    @SerializedName("userId") val userId: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("completed") val completed: Boolean
)
