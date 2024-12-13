package com.frxhaikal_plg.ingrevia.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.frxhaikal_plg.ingrevia.R
import com.frxhaikal_plg.ingrevia.data.local.UserPreferences
import com.frxhaikal_plg.ingrevia.data.remote.api.MLApiConfig
import com.frxhaikal_plg.ingrevia.data.remote.model.RecommendationRequest
import com.frxhaikal_plg.ingrevia.databinding.FragmentHomeBinding
import com.frxhaikal_plg.ingrevia.ui.home.adapter.IdealMenuAdapter
import com.frxhaikal_plg.ingrevia.ui.detailrecipes.RecipesDetailActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var userPreferences: UserPreferences
    private lateinit var loadingOverlay: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        userPreferences = UserPreferences(requireContext())
        
        // Inflate loading overlay
        loadingOverlay = inflater.inflate(R.layout.layout_loading, container, false)
        (binding.root as ViewGroup).addView(loadingOverlay)
        
        setupIdealMenuRecyclerView()
        return binding.root
    }

    private fun setupIdealMenuRecyclerView() {
        binding.rvidealmenu.layoutManager = LinearLayoutManager(
            context, 
            LinearLayoutManager.HORIZONTAL, 
            false
        )
        
        lifecycleScope.launch {
            try {
                showLoading(true, "Fetching recommendations...")
                val userInfo = getUserInfoForRecommendation()
                val response = MLApiConfig.getApiService().getRecommendations(userInfo)
                
                if (response.isSuccessful) {
                    response.body()?.recommendedRecipes?.let { recipes ->
                        val adapter = IdealMenuAdapter(
                            recipes.filterNotNull()
                        ) { recipe ->
                            // Navigate to detail
                            val intent = Intent(requireContext(), RecipesDetailActivity::class.java).apply {
                                putExtra(RECIPE_EXTRA, recipe)
                            }
                            startActivity(intent)
                        }
                        binding.rvidealmenu.adapter = adapter
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                showLoading(false)
            }
        }
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

    private fun showLoading(isLoading: Boolean, message: String = "") {
        loadingOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
        loadingOverlay.findViewById<TextView>(R.id.loadingText).text = message
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val RECIPE_EXTRA = "recipe_extra"
    }
}