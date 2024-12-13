package com.frxhaikal_plg.ingrevia.ui.detailrecipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.frxhaikal_plg.ingrevia.R
import com.frxhaikal_plg.ingrevia.data.remote.model.RecommendedRecipesItem
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.frxhaikal_plg.ingrevia.databinding.FragmentNutritionFactsBinding

class NutritionFactsFragment : Fragment() {
    private var _binding: FragmentNutritionFactsBinding? = null
    private val binding get() = _binding!!
    private var recipe: RecommendedRecipesItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNutritionFactsBinding.inflate(inflater, container, false)
        recipe = (activity as? RecipesDetailActivity)?.recipe
        setupNutritionInfo()
        return binding.root
    }

    private fun setupNutritionInfo() {
        recipe?.let { recipe ->
            val nutritionText = """
                Protein: ${recipe.protein}g
                Fat: ${recipe.fat}g
                Sodium: ${recipe.sodium}mg
                Calories: ${recipe.calories} cal
            """.trimIndent()
            
            binding.nutritionText.text = nutritionText
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
