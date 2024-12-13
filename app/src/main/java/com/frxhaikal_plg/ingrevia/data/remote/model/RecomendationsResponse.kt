package com.frxhaikal_plg.ingrevia.data.remote.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecomendationsResponse(

	@field:SerializedName("bmi_status")
	val bmiStatus: String? = null,

	@field:SerializedName("target_calories_per_meal")
	val targetCaloriesPerMeal: String? = null,

	@field:SerializedName("recommended_recipes")
	val recommendedRecipes: List<RecommendedRecipesItem?>? = null
) : Parcelable

@Parcelize
data class RecommendedRecipesItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("rating")
	val rating: Int? = null,

	@field:SerializedName("calories")
	val calories: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("Unnamed: 0")
	val unnamed0: Int? = null,

	@field:SerializedName("sodium")
	val sodium: Int? = null,

	@field:SerializedName("directions")
	val directions: String? = null,

	@field:SerializedName("protein")
	val protein: Int? = null,

	@field:SerializedName("fat")
	val fat: Int? = null,

	@field:SerializedName("ingredients")
	val ingredients: String? = null,

	@field:SerializedName("total_steps")
	val totalSteps: Int? = null,

	@field:SerializedName("categories")
	val categories: String? = null,

	@field:SerializedName("total_ingredients")
	val totalIngredients: Int? = null,

	@field:SerializedName("desc")
	val desc: String? = null
) : Parcelable
