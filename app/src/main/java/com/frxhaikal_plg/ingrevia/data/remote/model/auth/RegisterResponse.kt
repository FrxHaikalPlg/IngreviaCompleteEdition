package com.frxhaikal_plg.ingrevia.data.remote.model.auth

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: RegisterData
)

data class RegisterData(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val name: String
) 