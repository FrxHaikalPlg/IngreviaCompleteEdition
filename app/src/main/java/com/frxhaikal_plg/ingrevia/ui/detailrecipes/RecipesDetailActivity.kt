package com.frxhaikal_plg.ingrevia.ui.detailrecipes

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.frxhaikal_plg.ingrevia.R
import com.google.android.material.tabs.TabLayoutMediator
import com.bumptech.glide.Glide
import com.frxhaikal_plg.ingrevia.data.remote.model.RecommendedRecipesItem
import com.frxhaikal_plg.ingrevia.data.remote.model.home.RecipesItem
import com.frxhaikal_plg.ingrevia.databinding.ActivityRecipesDetailBinding
import com.frxhaikal_plg.ingrevia.ui.home.HomeFragment

class RecipesDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipesDetailBinding
    var recipe: RecommendedRecipesItem? = null
    var discoverRecipe: RecipesItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRecipesDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recipe = intent.getParcelableExtra(HomeFragment.RECIPE_EXTRA)
        discoverRecipe = intent.getParcelableExtra(HomeFragment.DISCOVER_RECIPE_EXTRA)
        
        setupViews()
        setupViewPager()
        setupBackButton()
    }

    private fun setupViews() {
        recipe?.let { setupRecommendedRecipe(it) } 
            ?: discoverRecipe?.let { setupDiscoverRecipe(it) }
    }

    private fun setupRecommendedRecipe(recipe: RecommendedRecipesItem) {
        binding.apply {
            profileTitle.text = recipe.title
            caloriesValue.text = "${recipe.calories} cal"
            stepNumber.text = "${recipe.totalSteps} steps"
            
            Glide.with(this@RecipesDetailActivity)
                .load(recipe.imageUrl)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .into(ivRecipePhoto)
        }
    }

    private fun setupDiscoverRecipe(recipe: RecipesItem) {
        binding.apply {
            profileTitle.text = recipe.title
            caloriesValue.text = "${recipe.calories} cal"
            stepNumber.text = "${recipe.totalSteps} steps"
            
            Glide.with(this@RecipesDetailActivity)
                .load(recipe.imageUrl)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .into(ivRecipePhoto)
        }
    }

    private fun setupViewPager() {
        val adapter = RecipesPagerAdapter(this, recipe, discoverRecipe)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Nutrition Facts"
                1 -> "Ingredients"
                2 -> "Steps"
                else -> null
            }
        }.attach()
    }

    private fun setupBackButton() {
        binding.backArrow.setOnClickListener {
            finish()
        }
    }
}