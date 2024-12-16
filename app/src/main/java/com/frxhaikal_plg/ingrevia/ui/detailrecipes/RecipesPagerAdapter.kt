package com.frxhaikal_plg.ingrevia.ui.detailrecipes

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.frxhaikal_plg.ingrevia.data.remote.model.RecommendedRecipesItem
import com.frxhaikal_plg.ingrevia.data.remote.model.home.RecipesItem

class RecipesPagerAdapter(
    activity: FragmentActivity,
    private val recommendedRecipe: RecommendedRecipesItem?,
    private val discoverRecipe: RecipesItem?
) : FragmentStateAdapter(activity) {
    private val fragments = listOf(
        NutritionFactsFragment(),
        IngredientsFragment(),
        StepsFragment()
    )

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}
