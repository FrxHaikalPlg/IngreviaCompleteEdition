package com.frxhaikal_plg.ingrevia.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.frxhaikal_plg.ingrevia.R
import com.frxhaikal_plg.ingrevia.data.local.UserPreferences
import com.frxhaikal_plg.ingrevia.data.remote.model.RecommendationRequest
import com.frxhaikal_plg.ingrevia.databinding.FragmentHomeBinding
import com.frxhaikal_plg.ingrevia.ui.home.adapter.IdealMenuAdapter
import com.frxhaikal_plg.ingrevia.ui.home.adapter.PopularMenuAdapter
import com.frxhaikal_plg.ingrevia.ui.detailrecipes.RecipesDetailActivity
import com.frxhaikal_plg.ingrevia.ui.recipes.SeeMoreRecipesActivity
import com.frxhaikal_plg.ingrevia.ui.recipes.SeeMorePopularRecipesActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private lateinit var userPreferences: UserPreferences
    private lateinit var loadingOverlay: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        userPreferences = UserPreferences(requireContext())
        
        setupLoadingOverlay(inflater, container)
        setupRecyclerView()
        setupPopularRecyclerView()
        observeViewModel()
        observePopularRecipes()
        fetchRecommendations()
        viewModel.getPopularRecipes()
        
        binding.seeMoreIdeal.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.recommendations.value?.fold(
                    onSuccess = { response ->
                        response.recommendedRecipes?.filterNotNull()?.let { recipes ->
                            val intent = Intent(requireContext(), SeeMoreRecipesActivity::class.java)
                            intent.putParcelableArrayListExtra(
                                SeeMoreRecipesActivity.EXTRA_RECIPES, 
                                ArrayList(recipes)
                            )
                            startActivity(intent)
                        }
                    },
                    onFailure = { e ->
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
        
        binding.seeMorePopular.setOnClickListener {
            val intent = Intent(requireContext(), SeeMorePopularRecipesActivity::class.java)
            startActivity(intent)
        }
        
        return binding.root
    }

    private fun setupLoadingOverlay(inflater: LayoutInflater, container: ViewGroup?) {
        loadingOverlay = inflater.inflate(R.layout.layout_loading, container, false)
        (binding.root as ViewGroup).addView(loadingOverlay)
        loadingOverlay.visibility = View.GONE
    }

    private fun setupRecyclerView() {
        binding.rvidealmenu.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
    }

    private fun setupPopularRecyclerView() {
        binding.rvpopularmenu.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                showLoading(isLoading)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.recommendations.collect { result ->
                result?.fold(
                    onSuccess = { response ->
                        response.recommendedRecipes?.filterNotNull()?.let { recipes ->
                            val adapter = IdealMenuAdapter(recipes) { recipe ->
                                val intent = Intent(requireContext(), RecipesDetailActivity::class.java)
                                intent.putExtra(RECIPE_EXTRA, recipe)
                                startActivity(intent)
                            }
                            binding.rvidealmenu.adapter = adapter
                        }
                    },
                    onFailure = { e ->
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    private fun observePopularRecipes() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.popularRecipes.collect { result ->
                result?.fold(
                    onSuccess = { response ->
                        response.data?.recipes?.filterNotNull()?.let { recipes ->
                            val adapter = PopularMenuAdapter(recipes) { recipe ->
                                val intent = Intent(requireContext(), RecipesDetailActivity::class.java)
                                intent.putExtra(DISCOVER_RECIPE_EXTRA, recipe)
                                startActivity(intent)
                            }
                            binding.rvpopularmenu.adapter = adapter
                        }
                    },
                    onFailure = { e ->
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    private fun fetchRecommendations() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val userInfo = getUserInfoForRecommendation()
                viewModel.getRecommendations(userInfo)
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        loadingOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private suspend fun getUserInfoForRecommendation(): RecommendationRequest {
        // Convert gender string to int (1 for male, 0 for female)
        val genderInt = when(userPreferences.gender.first()) {
            "Male" -> 1
            else -> 2
        }
        
        return RecommendationRequest(
            weight = userPreferences.weight.first().toInt(),
            height = userPreferences.height.first().toInt(),
            age = userPreferences.age.first().toInt(),
            gender = genderInt,
            activityLevel = userPreferences.activityLevel.first().toInt()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val RECIPE_EXTRA = "recipe_extra"
        const val DISCOVER_RECIPE_EXTRA = "discover_recipe_extra"
    }
}