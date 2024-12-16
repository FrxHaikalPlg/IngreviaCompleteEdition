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
import com.frxhaikal_plg.ingrevia.databinding.FragmentStepsBinding

class StepsFragment : Fragment() {
    private var _binding: FragmentStepsBinding? = null
    private val binding get() = _binding!!
    private var recipe: RecommendedRecipesItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStepsBinding.inflate(inflater, container, false)
        recipe = (activity as? RecipesDetailActivity)?.recipe
        setupSteps()
        return binding.root
    }

    private fun setupSteps() {
        recipe?.directions?.let { directions ->
            // Split steps into a list if they are a single string separated by periods or other delimiters
            val steps = directions.split(".").map { it.trim() }
            binding.stepsContainer.removeAllViews()

            steps.forEach { step ->
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
                    setBackgroundColor(resources.getColor(R.color.primary_200, null)) // Warna kotak
                    layoutParams = LinearLayout.LayoutParams(12.dpToPx(), 12.dpToPx()).apply {
                        setMargins(0, 0, 16.dpToPx(), 0) // Jarak antara kotak dan teks langkah
                    }
                }

                val textView = TextView(requireContext()).apply {
                    text = step
                    setTextAppearance(R.style.regular_14)
                    setTextColor(resources.getColor(R.color.neutral_600, null))
                }

                itemLayout.addView(pointView)
                itemLayout.addView(textView)
                binding.stepsContainer.addView(itemLayout)
            }
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