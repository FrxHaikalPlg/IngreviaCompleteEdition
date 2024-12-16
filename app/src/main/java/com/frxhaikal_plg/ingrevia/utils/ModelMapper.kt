package com.frxhaikal_plg.ingrevia.utils

import com.frxhaikal_plg.ingrevia.data.remote.model.home.RecipesItem
import com.frxhaikal_plg.ingrevia.data.remote.model.search.DataItem

fun DataItem.toRecipesItem(): RecipesItem {
    return RecipesItem(
        id = this.id,
        title = this.title,
        imageUrl = this.imageUrl,
        calories = this.calories,
        totalSteps = this.totalSteps,
        protein = this.protein,
        ingredients = this.ingredients,
        directions = this.directions,
        fat = this.fat,
        date = this.date,
        rating = this.rating?.toString(),
        sodium = this.sodium,
        categories = this.categories,
        totalIngredients = this.totalIngredients,
        desc = this.desc
    )
} 