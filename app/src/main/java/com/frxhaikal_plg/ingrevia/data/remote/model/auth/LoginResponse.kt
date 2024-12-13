package com.frxhaikal_plg.ingrevia.data.remote.model.auth

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: LoginData
)

data class LoginData(
    @SerializedName("token")
    val token: String,
    @SerializedName("user")
    val user: UserData
)

data class UserData(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("displayName")
    val displayName: String
)
