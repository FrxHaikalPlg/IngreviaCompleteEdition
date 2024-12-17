package com.frxhaikal_plg.ingrevia.data.remote.model.favorite

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class GetFavoriteResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class DataItem(

	@field:SerializedName("createdAt")
	val createdAt: CreatedAt? = null,

	@field:SerializedName("recipe")
	val recipe: Recipe? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("recipeId")
	val recipeId: String? = null
) : Parcelable

@Parcelize
data class CreatedAt(

	@field:SerializedName("_nanoseconds")
	val nanoseconds: Int? = null,

	@field:SerializedName("_seconds")
	val seconds: Int? = null
) : Parcelable

@Parcelize
data class Recipe(

	@field:SerializedName("date")
	val date: Long? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("rating")
	val rating: Double? = null,

	@field:SerializedName("calories")
	val calories: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("sodium")
	val sodium: Int? = null,

	@field:SerializedName("directions")
	val directions: List<String?>? = null,

	@field:SerializedName("protein")
	val protein: Int? = null,

	@field:SerializedName("fat")
	val fat: Int? = null,

	@field:SerializedName("ingredients")
	val ingredients: List<String?>? = null,

	@field:SerializedName("total_steps")
	val totalSteps: Int? = null,

	@field:SerializedName("categories")
	val categories: List<String?>? = null,

	@field:SerializedName("total_ingredients")
	val totalIngredients: Int? = null,

	@field:SerializedName("desc")
	val desc: String? = null
) : Parcelable
