package com.frxhaikal_plg.ingrevia.data.remote.model.auth

import com.google.gson.annotations.SerializedName

data class ForgotPasswordResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: ForgotPasswordData?
)

data class ForgotPasswordData(
    @SerializedName("email")
    val email: String,
    @SerializedName("resetLink")
    val resetLink: String
) 