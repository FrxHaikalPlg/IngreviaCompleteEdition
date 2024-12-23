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
import com.frxhaikal_plg.ingrevia.databinding.FragmentStepsBinding
import android.view.Gravity

class StepsFragment : Fragment() {
    private var _binding: FragmentStepsBinding? = null
    private val binding get() = _binding!!
    private var recommendedRecipe: RecommendedRecipesItem? = null
    private var discoverRecipe: RecipesItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStepsBinding.inflate(inflater, container, false)
        recommendedRecipe = (activity as? RecipesDetailActivity)?.recipe
        discoverRecipe = (activity as? RecipesDetailActivity)?.discoverRecipe
        setupSteps()
        return binding.root
    }

    private fun setupSteps() {
        when {
            recommendedRecipe != null -> setupRecommendedSteps()
            discoverRecipe != null -> setupDiscoverSteps()
        }
    }

    private fun setupRecommendedSteps() {
        recommendedRecipe?.directions?.let { directions ->
            try {
                val cleanDirections = directions.trim('[', ']')
                val steps = cleanDirections.split("', '")
                    .map { it.trim('\'') }
                    .filter { it.isNotEmpty() }
                displaySteps(steps)
            } catch (e: Exception) {
                val steps = directions.split(".").map { it.trim() }
                displaySteps(steps)
            }
        }
    }

    private fun setupDiscoverSteps() {
        discoverRecipe?.directions?.filterNotNull()?.let { steps ->
            displaySteps(steps)
        }
    }

    private fun displaySteps(steps: List<String>) {
        binding.stepsContainer.removeAllViews()
        steps.forEachIndexed { index, step ->
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
                setBackgroundColor(resources.getColor(R.color.primary_200, null))
                layoutParams = LinearLayout.LayoutParams(12.dpToPx(), 12.dpToPx()).apply {
                    setMargins(0, 4.dpToPx(), 16.dpToPx(), 0)
                    gravity = Gravity.TOP
                }
            }

            val textView = TextView(requireContext()).apply {
                text = step
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
            binding.stepsContainer.addView(itemLayout)
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