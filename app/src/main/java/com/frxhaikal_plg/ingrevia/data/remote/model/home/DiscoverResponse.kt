package com.frxhaikal_plg.ingrevia.data.remote.model.home

import com.google.gson.annotations.SerializedName
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class DiscoverResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Pagination(

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("limit")
	val limit: Int? = null,

	@field:SerializedName("totalPages")
	val totalPages: Int? = null,

	@field:SerializedName("page")
	val page: Int? = null
)

@Parcelize
data class RecipesItem(

	@field:SerializedName("date")
	val date: Long? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("rating")
	val rating: String? = null,

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

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("categories")
	val categories: List<String?>? = null,

	@field:SerializedName("total_ingredients")
	val totalIngredients: Int? = null,

	@field:SerializedName("desc")
	val desc: String? = null
) : Parcelable

data class Data(

	@field:SerializedName("recipes")
	val recipes: List<RecipesItem?>? = null,

	@field:SerializedName("pagination")
	val pagination: Pagination? = null
)
