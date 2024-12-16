package com.frxhaikal_plg.ingrevia.ui.recipes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.frxhaikal_plg.ingrevia.data.remote.model.RecommendedRecipesItem
import com.frxhaikal_plg.ingrevia.databinding.ActivitySeeMoreRecipesBinding
import com.frxhaikal_plg.ingrevia.ui.recipes.adapter.SeeMoreRecipesAdapter

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
            adapter = recipes?.let { SeeMoreRecipesAdapter(it) }
        }
    }

    companion object {
        const val EXTRA_RECIPES = "extra_recipes"
    }
}