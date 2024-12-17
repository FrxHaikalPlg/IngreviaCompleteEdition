package com.frxhaikal_plg.ingrevia.data.remote.model.favorite

import com.google.gson.annotations.SerializedName

data class AddFavoriteRequest(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("recipeId")
    val recipeId: String
)

data class BaseResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String
)
