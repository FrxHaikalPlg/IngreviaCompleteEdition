package com.frxhaikal_plg.ingrevia.ui.detailrecipes

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.frxhaikal_plg.ingrevia.R
import com.frxhaikal_plg.ingrevia.data.remote.model.RecommendedRecipesItem
import com.frxhaikal_plg.ingrevia.data.remote.model.home.RecipesItem
import com.frxhaikal_plg.ingrevia.databinding.FragmentIngredientsBinding
import android.view.Gravity

class IngredientsFragment : Fragment() {
    private var _binding: FragmentIngredientsBinding? = null
    private val binding get() = _binding!!
    private var recommendedRecipe: RecommendedRecipesItem? = null
    private var discoverRecipe: RecipesItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIngredientsBinding.inflate(inflater, container, false)
        recommendedRecipe = (activity as? RecipesDetailActivity)?.recipe
        discoverRecipe = (activity as? RecipesDetailActivity)?.discoverRecipe
        setupIngredients()
        return binding.root
    }

    private fun setupIngredients() {
        when {
            recommendedRecipe != null -> setupRecommendedIngredients()
            discoverRecipe != null -> setupDiscoverIngredients()
        }
    }

    private fun setupRecommendedIngredients() {
        recommendedRecipe?.ingredients?.let { ingredientsList ->
            try {
                val cleanIngredients = ingredientsList.trim('[', ']')
                val ingredients = cleanIngredients.split("', '")
                    .map { it.trim('\'') }
                    .filter { it.isNotEmpty() }
                displayIngredients(ingredients)
            } catch (e: Exception) {
                val ingredients = ingredientsList.split(",").map { it.trim() }
                displayIngredients(ingredients)
            }
        }
    }

    private fun setupDiscoverIngredients() {
        discoverRecipe?.ingredients?.filterNotNull()?.let { ingredients ->
            displayIngredients(ingredients)
        }
    }

    private fun displayIngredients(ingredients: List<String>) {
        binding.ingredientsContainer.removeAllViews()
        ingredients.forEach { ingredient ->
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
                layoutParams = LinearLayout.LayoutParams(8.dpToPx(), 8.dpToPx()).apply {
                    setMargins(0, 6.dpToPx(), 16.dpToPx(), 0)
                    gravity = Gravity.TOP
                }
                setBackgroundColor(resources.getColor(R.color.primary_200, null))
            }

            val textView = TextView(requireContext()).apply {
                text = ingredient
                setTextAppearance(R.style.regular_14)
                setTextColor(resources.getColor(R.color.neutral_600, null))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                gravity = Gravity.FILL_HORIZONTAL
            }

            itemLayout.addView(pointView)
            itemLayout.addView(textView)
            binding.ingredientsContainer.addView(itemLayout)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }
}
