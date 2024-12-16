package com.frxhaikal_plg.ingrevia.ui.detailrecipes

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.frxhaikal_plg.ingrevia.R
import com.frxhaikal_plg.ingrevia.data.remote.model.RecommendedRecipesItem
import com.frxhaikal_plg.ingrevia.data.remote.model.home.RecipesItem
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.frxhaikal_plg.ingrevia.databinding.FragmentNutritionFactsBinding

class NutritionFactsFragment : Fragment() {
    private var _binding: FragmentNutritionFactsBinding? = null
    private val binding get() = _binding!!
    private var recommendedRecipe: RecommendedRecipesItem? = null
    private var discoverRecipe: RecipesItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNutritionFactsBinding.inflate(inflater, container, false)
        recommendedRecipe = (activity as? RecipesDetailActivity)?.recipe
        discoverRecipe = (activity as? RecipesDetailActivity)?.discoverRecipe
        setupNutritionInfo()
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setupNutritionInfo() {
        val nutritionData = when {
            recommendedRecipe != null -> listOf(
                Pair("Protein", "${recommendedRecipe?.protein}g"),
                Pair("Fat", "${recommendedRecipe?.fat}g"),
                Pair("Sodium", "${recommendedRecipe?.sodium}mg"),
                Pair("Calories", "${recommendedRecipe?.calories} cal")
            )
            discoverRecipe != null -> listOf(
                Pair("Protein", "${discoverRecipe?.protein}g"),
                Pair("Fat", "${discoverRecipe?.fat}g"),
                Pair("Sodium", "${discoverRecipe?.sodium}mg"),
                Pair("Calories", "${discoverRecipe?.calories} cal")
            )
            else -> emptyList()
        }

        binding.nutritionContainer.removeAllViews()

        for ((label, value) in nutritionData) {
            val itemLayout = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 8, 0, 8)
                }
            }

            val pointView = View(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(16.dpToPx(), 16.dpToPx()).apply {
                    setMargins(0, 0, 16.dpToPx(), 0)
                }
                setBackgroundColor(resources.getColor(R.color.primary_200, null))
            }

            val textView = TextView(requireContext()).apply {
                text = "$label: $value"
                setTextAppearance(R.style.regular_14)
                setTextColor(resources.getColor(R.color.neutral_600, null))
            }

            itemLayout.addView(pointView)
            itemLayout.addView(textView)
            binding.nutritionContainer.addView(itemLayout)
        }
    }

    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
