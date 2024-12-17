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
import com.frxhaikal_plg.ingrevia.ui.favorite.FavoriteViewModel
import androidx.lifecycle.lifecycleScope
import androidx.core.content.ContextCompat
import androidx.activity.viewModels
import com.frxhaikal_plg.ingrevia.data.local.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RecipesDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipesDetailBinding
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private var isFavorite = false
    private lateinit var userPreferences: UserPreferences
    private var userId: String? = null
    private var currentRecipeId: String? = null
    var recipe: RecommendedRecipesItem? = null
    var discoverRecipe: RecipesItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRecipesDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userPreferences = UserPreferences(this)

        // Ambil data dari intent
        recipe = intent.getParcelableExtra(HomeFragment.RECIPE_EXTRA)
        discoverRecipe = intent.getParcelableExtra(HomeFragment.DISCOVER_RECIPE_EXTRA)

        lifecycleScope.launch {
            userId = userPreferences.userId.first()
            setupFavoriteButton()
            checkIfFavorite()
        }

        // Setup favorite button click
        binding.imageViewFavorite.setOnClickListener {
            if (userId != null && currentRecipeId != null) {
                if (isFavorite) {
                    favoriteViewModel.deleteFavorite(userId!!, currentRecipeId!!)
                } else {
                    favoriteViewModel.addFavorite(userId!!, currentRecipeId!!)
                }
                isFavorite = !isFavorite
                updateFavoriteIcon()
            }
        }

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

    private fun setupFavoriteButton() {
        recipe?.let {
            currentRecipeId = "recipe_${it.unnamed0?.plus(1)}"
        } ?: discoverRecipe?.let {
            currentRecipeId = it.id
        }
    }

    private fun checkIfFavorite() {
        userId?.let { uid ->
            favoriteViewModel.getFavorites(uid)
        }
        
        lifecycleScope.launch {
            favoriteViewModel.favorites.collect { result ->
                result?.fold(
                    onSuccess = { response ->
                        isFavorite = response.data?.any { it?.recipeId == currentRecipeId } == true
                        updateFavoriteIcon()
                    },
                    onFailure = {
                        // Handle error
                    }
                )
            }
        }
    }

    private fun updateFavoriteIcon() {
        binding.imageViewFavorite.setColorFilter(
            ContextCompat.getColor(
                this,
                if (isFavorite) R.color.primary_200 else R.color.neutral_100
            )
        )
    }
}