package com.frxhaikal_plg.ingrevia.data.remote.model

import com.google.gson.annotations.SerializedName

data class RecommendationRequest(
    @SerializedName("weight")
    val weight: Int,
    @SerializedName("height") 
    val height: Int,
    @SerializedName("age")
    val age: Int,
    @SerializedName("gender")
    val gender: Int,
    @SerializedName("activity_level")
    val activityLevel: Int,
    @SerializedName("liked_recipe_indices")
    val likedRecipeIndices: List<Int> = emptyList()
) 