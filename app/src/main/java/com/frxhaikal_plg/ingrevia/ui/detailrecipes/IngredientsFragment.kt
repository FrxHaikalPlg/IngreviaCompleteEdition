package com.frxhaikal_plg.ingrevia.ui.detailrecipes

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import com.frxhaikal_plg.ingrevia.R
import com.frxhaikal_plg.ingrevia.data.remote.model.RecommendedRecipesItem
import com.frxhaikal_plg.ingrevia.databinding.FragmentIngredientsBinding

class IngredientsFragment : Fragment() {
    private var _binding: FragmentIngredientsBinding? = null
    private val binding get() = _binding!!
    private var recipe: RecommendedRecipesItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIngredientsBinding.inflate(inflater, container, false)
        recipe = (activity as? RecipesDetailActivity)?.recipe
        setupIngredients()
        return binding.root
    }

    private fun setupIngredients() {
        recipe?.ingredients?.let { ingredients ->
            binding.ingredientsText.text = ingredients
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}