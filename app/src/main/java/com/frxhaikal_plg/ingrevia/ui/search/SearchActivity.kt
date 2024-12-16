package com.frxhaikal_plg.ingrevia.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.frxhaikal_plg.ingrevia.databinding.ActivitySearchBinding
import com.frxhaikal_plg.ingrevia.ui.detailrecipes.RecipesDetailActivity
import com.frxhaikal_plg.ingrevia.ui.home.HomeFragment
import com.frxhaikal_plg.ingrevia.ui.search.adapter.SearchResultAdapter
import com.frxhaikal_plg.ingrevia.utils.toRecipesItem
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var adapter: SearchResultAdapter

    @OptIn(FlowPreview::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupRecyclerView()
        observeViewModel()

        // Fokuskan search view dan tampilkan keyboard
        binding.searchView.apply {
            isIconified = false
            requestFocusFromTouch()
        }
    }

    private fun setupViews() {
        binding.backArrow.setOnClickListener { finish() }
        
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchRecipes(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Tidak melakukan pencarian saat mengetik
                return true
            }
        })
    }

    private fun setupRecyclerView() {
        adapter = SearchResultAdapter { recipe ->
            val intent = Intent(this, RecipesDetailActivity::class.java)
            intent.putExtra(HomeFragment.DISCOVER_RECIPE_EXTRA, recipe.toRecipesItem())
            startActivity(intent)
        }

        binding.rvsearch.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = this@SearchActivity.adapter
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.loadingLayout.root.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }

        lifecycleScope.launch {
            viewModel.searchResults.collect { result ->
                result?.fold(
                    onSuccess = { response ->
                        if (response.success == true) {
                            if (response.data.isNullOrEmpty()) {
                                showEmptyState("No recipes found", "Try searching with different keywords")
                            } else {
                                hideEmptyState()
                                response.data.filterNotNull().let { recipes ->
                                    adapter.submitList(recipes)
                                }
                            }
                        } else {
                            showError("Failed to get recipes")
                        }
                    },
                    onFailure = { e ->
                        when {
                            e is java.net.UnknownHostException -> {
                                showEmptyState(
                                    "No internet connection",
                                    "Please check your internet connection and try again"
                                )
                            }
                            e is java.net.SocketTimeoutException -> {
                                showEmptyState(
                                    "Connection timeout",
                                    "Please try again"
                                )
                            }
                            e.message?.contains("404") == true -> {
                                showEmptyState(
                                    "Recipe not found",
                                    "We couldn't find any recipes matching your search"
                                )
                            }
                            else -> {
                                showEmptyState(
                                    "Something went wrong",
                                    "Please try again later"
                                )
                            }
                        }
                    }
                )
            }
        }
    }

    private fun showEmptyState(title: String, description: String) {
        binding.apply {
            rvsearch.visibility = View.GONE
            emptyState.apply {
                root.visibility = View.VISIBLE
                emptyText.text = title
                emptyDescription.text = description
            }
        }
    }

    private fun hideEmptyState() {
        binding.apply {
            rvsearch.visibility = View.VISIBLE
            emptyState.root.visibility = View.GONE
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this@SearchActivity, message, Toast.LENGTH_SHORT).show()
        showEmptyState(
            "Oops!",
            message
        )
    }
}