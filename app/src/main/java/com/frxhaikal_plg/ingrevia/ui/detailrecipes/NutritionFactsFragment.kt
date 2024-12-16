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

    @SuppressLint("SetTextI18n")
    private fun setupNutritionInfo() {
        recipe?.let { recipe ->
            val nutritionData = listOf(
                Pair("Protein", "${recipe.protein}g"),
                Pair("Fat", "${recipe.fat}g"),
                Pair("Sodium", "${recipe.sodium}mg"),
                Pair("Calories", "${recipe.calories} cal")
            )

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
    }

    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
