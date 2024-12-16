package com.frxhaikal_plg.ingrevia.ui.recipes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.frxhaikal_plg.ingrevia.databinding.ActivitySeeMorePopularRecipesBinding
import com.frxhaikal_plg.ingrevia.ui.detailrecipes.RecipesDetailActivity
import com.frxhaikal_plg.ingrevia.ui.home.HomeFragment
import com.frxhaikal_plg.ingrevia.ui.recipes.adapter.SeeMorePopularRecipesAdapter
import kotlinx.coroutines.launch

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
        observeViewModel()
        viewModel.getDiscoverRecipes()
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
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (!isLoading && 
                        (visibleItemCount + firstVisibleItemPosition) >= totalItemCount &&
                        firstVisibleItemPosition >= 0) {
                        viewModel.loadNextPage()
                    }
                }
            })
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.isLoading.collect { loading ->
                isLoading = loading
                binding.loadingLayout.root.visibility = if (loading) View.VISIBLE else View.GONE
                binding.loadingLayout.loadingText.text = "Loading recipes..."
            }
        }

        lifecycleScope.launch {
            viewModel.recipes.collect { result ->
                result?.fold(
                    onSuccess = { response ->
                        response.data?.recipes?.filterNotNull()?.let { recipes ->
                            adapter.submitList(recipes)
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
    }
}