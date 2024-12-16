package com.frxhaikal_plg.ingrevia.ui.recipes

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.frxhaikal_plg.ingrevia.R
import com.frxhaikal_plg.ingrevia.databinding.ActivitySeeMorePopularRecipesBinding
import com.frxhaikal_plg.ingrevia.ui.detailrecipes.RecipesDetailActivity
import com.frxhaikal_plg.ingrevia.ui.home.HomeFragment
import com.frxhaikal_plg.ingrevia.ui.recipes.adapter.SeeMorePopularRecipesAdapter
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.combine

class SeeMorePopularRecipesActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySeeMorePopularRecipesBinding
    private val viewModel: SeeMorePopularViewModel by viewModels()
    private lateinit var adapter: SeeMorePopularRecipesAdapter
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeeMorePopularRecipesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupRecyclerView()
        setupPagination()
        observeViewModel()
    }

    private fun setupViews() {
        binding.backArrow.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        adapter = SeeMorePopularRecipesAdapter { recipe ->
            val intent = Intent(this, RecipesDetailActivity::class.java)
            intent.putExtra(HomeFragment.DISCOVER_RECIPE_EXTRA, recipe)
            startActivity(intent)
        }

        binding.rvRecipes.apply {
            layoutManager = GridLayoutManager(this@SeeMorePopularRecipesActivity, 2)
            adapter = this@SeeMorePopularRecipesActivity.adapter
        }
    }

    private fun setupPagination() {
        binding.apply {
            paginationLayout.visibility = View.VISIBLE
            btnFirst.setOnClickListener { viewModel.goToFirstPage() }
            btnLast.setOnClickListener { viewModel.goToLastPage() }
            btnPrev.setOnClickListener { viewModel.prevPage() }
            btnNext.setOnClickListener { viewModel.nextPage() }
        }
    }

    private fun updatePaginationUI(currentPage: Int, totalPages: Int) {
        binding.paginationLayout.visibility = View.VISIBLE
        binding.apply {
            btnFirst.visibility = if (currentPage > 1) View.VISIBLE else View.GONE
            btnPrev.visibility = if (currentPage > 1) View.VISIBLE else View.GONE
            btnNext.visibility = if (currentPage < totalPages) View.VISIBLE else View.GONE
            btnLast.visibility = if (currentPage < totalPages) View.VISIBLE else View.GONE
        }
        
        binding.pageNumbersContainer.removeAllViews()
        
        // Tampilkan maksimal 5 nomor halaman
        val start = maxOf(1, currentPage - 2)
        val end = minOf(totalPages, start + 4)
        
        for (i in start..end) {
            val pageButton = TextView(this).apply {
                text = i.toString()
                textSize = 16f
                setTextColor(if (i == currentPage) 
                    getColor(R.color.primary_200) 
                    else getColor(R.color.neutral_500))
                setPadding(24, 0, 24, 0)
                gravity = Gravity.CENTER
                setOnClickListener { viewModel.goToPage(i) }
            }
            binding.pageNumbersContainer.addView(pageButton)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            combine(
                viewModel.currentPage,
                viewModel.totalPages,
                viewModel.recipes
            ) { currentPage, totalPages, recipes ->
                Triple(currentPage, totalPages, recipes)
            }.collect { (currentPage, totalPages, recipes) ->
                updatePaginationUI(currentPage, totalPages)
                
                recipes?.fold(
                    onSuccess = { response ->
                        response.data?.recipes?.filterNotNull()?.let { recipesList ->
                            adapter.submitList(recipesList)
                        }
                    },
                    onFailure = { e ->
                        Toast.makeText(this@SeeMorePopularRecipesActivity, 
                            "Error: ${e.message}", 
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect { loading ->
                isLoading = loading
                binding.loadingLayout.root.visibility = if (loading) View.VISIBLE else View.GONE
                binding.loadingLayout.loadingText.text = "Loading recipes..."
            }
        }
    }
}