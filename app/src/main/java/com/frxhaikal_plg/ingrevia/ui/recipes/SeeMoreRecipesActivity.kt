package com.frxhaikal_plg.ingrevia.ui.recipes

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.frxhaikal_plg.ingrevia.data.remote.model.RecommendedRecipesItem
import com.frxhaikal_plg.ingrevia.databinding.ActivitySeeMoreRecipesBinding
import com.frxhaikal_plg.ingrevia.ui.detailrecipes.RecipesDetailActivity
import com.frxhaikal_plg.ingrevia.ui.recipes.adapter.SeeMoreRecipesAdapter
import com.frxhaikal_plg.ingrevia.ui.home.HomeFragment

class SeeMoreRecipesActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySeeMoreRecipesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeeMoreRecipesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupRecyclerView()
    }

    private fun setupViews() {
        binding.backArrow.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        val recipes = intent.getParcelableArrayListExtra<RecommendedRecipesItem>(EXTRA_RECIPES)
        
        binding.rvRecipes.apply {
            layoutManager = GridLayoutManager(this@SeeMoreRecipesActivity, 2)
            adapter = recipes?.let { 
                SeeMoreRecipesAdapter(it) { selectedRecipe ->
                    // Handle click dan navigasi ke detail
                    val intent = Intent(this@SeeMoreRecipesActivity, RecipesDetailActivity::class.java)
                    intent.putExtra(HomeFragment.RECIPE_EXTRA, selectedRecipe)
                    startActivity(intent)
                }
            }
        }
    }

    companion object {
        const val EXTRA_RECIPES = "extra_recipes"
    }
}