package com.frxhaikal_plg.ingrevia.ui.recipes

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.frxhaikal_plg.ingrevia.R
import com.frxhaikal_plg.ingrevia.databinding.FragmentRecipesBinding
import com.google.android.material.chip.Chip

class RecipesFragment : Fragment() {
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!
    private val selectedIngredients = mutableSetOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        setupSearchView()
        setupChips()
        return binding.root
    }

    private fun setupSearchView() {
        binding.searchBar.apply {
            // Setup search functionality if needed
        }
    }

    private fun createSelectedChip(ingredient: String): Chip {
        return Chip(requireContext()).apply {
            text = ingredient
            isCloseIconVisible = true
            setCloseIconResource(R.drawable.ic_close)
            closeIconTint = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.neutral_500))
            setOnCloseIconClickListener {
                binding.selectedChipsGroup.removeView(this)
                findAndUncheckIngredientChip(ingredient)
                selectedIngredients.remove(ingredient)
            }
            setChipBackgroundColorResource(R.color.chip_background)
            setTextColor(ContextCompat.getColor(context, R.color.chip_text))
        }
    }

    private fun findAndUncheckIngredientChip(ingredient: String) {
        for (i in 0 until binding.ingredientsChipGroup.childCount) {
            val chip = binding.ingredientsChipGroup.getChildAt(i) as? Chip
            if (chip?.text == ingredient) {
                chip.isChecked = false
                break
            }
        }
    }

    private fun setupChips() {
        val ingredients = listOf(
            "chicken", "beef", "fish", "pork", "rice", "pasta", "potato",
            "tomato", "onion", "garlic", "carrot", "mushroom", "egg",
            "milk", "cheese", "butter", "olive oil", "soy sauce", "salt",
            "pepper", "sugar", "flour", "bread"
        )
        
        ingredients.forEach { ingredient ->
            val chip = Chip(requireContext()).apply {
                text = ingredient
                isCheckable = true
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        selectedIngredients.add(ingredient)
                        binding.selectedChipsGroup.addView(createSelectedChip(ingredient))
                    } else {
                        selectedIngredients.remove(ingredient)
                        removeSelectedChip(ingredient)
                    }
                }
            }
            binding.ingredientsChipGroup.addView(chip)
        }
    }

    private fun removeSelectedChip(ingredient: String) {
        for (i in 0 until binding.selectedChipsGroup.childCount) {
            val chip = binding.selectedChipsGroup.getChildAt(i) as? Chip
            if (chip?.text == ingredient) {
                binding.selectedChipsGroup.removeView(chip)
                break
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}