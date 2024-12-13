package com.frxhaikal_plg.ingrevia.ui.detailrecipes

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.frxhaikal_plg.ingrevia.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.bumptech.glide.Glide
import com.frxhaikal_plg.ingrevia.data.remote.model.RecommendedRecipesItem
import com.frxhaikal_plg.ingrevia.databinding.ActivityRecipesDetailBinding
import com.frxhaikal_plg.ingrevia.ui.home.HomeFragment

class RecipesDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipesDetailBinding
    var recipe: RecommendedRecipesItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRecipesDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recipe = intent.getParcelableExtra(HomeFragment.RECIPE_EXTRA)
        
        setupViews()
        setupViewPager()
        setupBackButton()
    }

    private fun setupViews() {
        recipe?.let { recipe ->
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
    }

    private fun setupViewPager() {
        val adapter = RecipesPagerAdapter(this, recipe)
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