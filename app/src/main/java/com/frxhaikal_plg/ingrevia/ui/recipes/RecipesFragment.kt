package com.frxhaikal_plg.ingrevia.ui.recipes

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.frxhaikal_plg.ingrevia.R
import com.frxhaikal_plg.ingrevia.databinding.FragmentRecipesBinding
import com.frxhaikal_plg.ingrevia.ui.detailrecipes.RecipesDetailActivity
import com.frxhaikal_plg.ingrevia.ui.home.HomeFragment
import com.frxhaikal_plg.ingrevia.ui.search.adapter.SearchResultAdapter
import com.frxhaikal_plg.ingrevia.utils.toRecipesItem
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch

class RecipesFragment : Fragment() {
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!
    private val selectedIngredients = mutableSetOf<String>()
    private val viewModel: RecipesViewModel by viewModels()
    private lateinit var searchAdapter: SearchResultAdapter
    private val allIngredients = listOf(
        "absinthe", "almond", "anchovy", "apple", "applesauce", "apricot", "arugula", "artichoke", 
        "asparagus", "avocado", "bacon", "banana", "barley", "bean", "beef", "beer", "beet", 
        "blackberry", "blueberry", "bread", "breadcrumb", "broccoli", "broth", "butter", "buttermilk",
        "cabbage", "cacao", "cake", "carrot", "cashew", "cauliflower", "celery", "cheese", "cherry",
        "chicken", "chili", "chive", "chocolate", "cinnamon", "clam", "coconut", "coconut milk", "cod",
        "coffee", "corn", "cream", "cucumber", "cumin", "date", "dill", "duck", "egg", "eggplant",
        "fig", "fish", "flour", "garlic", "ginger", "goat cheese", "grape", "green bean", "honey",
        "kale", "lamb", "leek", "lemon", "lettuce", "lime", "lobster", "mango", "maple syrup", "milk",
        "mushroom", "mustard", "nutmeg", "oat", "oil", "olive oil", "onion", "orange", "pasta",
        "peach", "peanut", "peanut butter", "pear", "pea", "pepper", "peppercorn", "pineapple",
        "plum", "pork", "potato", "pumpkin", "quinoa", "radish", "raspberry", "rice", "rosemary",
        "salmon", "salt", "sardine", "sausage", "scallion", "scallop", "shallot", "shrimp",
        "soy sauce", "spinach", "squash", "strawberry", "sugar", "sweet potato", "thyme", "tofu",
        "tomato", "trout", "tuna", "turkey", "vanilla", "vinegar", "walnut", "water", "watermelon",
        "whiskey", "wine", "yogurt", "zucchini"
    )
    private var filteredIngredients = allIngredients

    private companion object {
        const val MAX_INGREDIENTS = 10
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        setupSearchView()
        setupFilteredChips()
        setupRecyclerView()
        observeViewModel()
        return binding.root
    }

    private fun setupSearchView() {
        binding.textInputLayout.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterIngredients(s?.toString() ?: "")
            }
            
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.imageViewFavorite.setOnClickListener {
            if (selectedIngredients.isNotEmpty()) {
                viewModel.searchRecipesByIngredients(selectedIngredients.toList())
            } else {
                Toast.makeText(
                    requireContext(),
                    "Pilih setidaknya satu bahan",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun filterIngredients(query: String) {
        binding.ingredientsChipGroup.removeAllViews()
        filteredIngredients = if (query.isEmpty()) {
            allIngredients
        } else {
            allIngredients.filter { it.contains(query, ignoreCase = true) }
        }
        setupFilteredChips()
    }

    private fun setupFilteredChips() {
        filteredIngredients.forEach { ingredient ->
            val chip = createChip(ingredient)
            binding.ingredientsChipGroup.addView(chip)
        }
    }

    private fun createChip(ingredient: String): Chip {
        return Chip(requireContext()).apply {
            text = ingredient
            isCheckable = true
            isChecked = selectedIngredients.contains(ingredient)
            setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    if (selectedIngredients.size >= MAX_INGREDIENTS) {
                        buttonView.isChecked = false
                        Toast.makeText(
                            requireContext(),
                            "Maksimal $MAX_INGREDIENTS bahan yang dapat dipilih",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        selectedIngredients.add(ingredient)
                        binding.selectedChipsGroup.addView(createSelectedChip(ingredient))
                    }
                } else {
                    selectedIngredients.remove(ingredient)
                    removeSelectedChip(ingredient)
                }
            }
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

    private fun removeSelectedChip(ingredient: String) {
        for (i in 0 until binding.selectedChipsGroup.childCount) {
            val chip = binding.selectedChipsGroup.getChildAt(i) as? Chip
            if (chip?.text == ingredient) {
                binding.selectedChipsGroup.removeView(chip)
                break
            }
        }
    }

    private fun setupRecyclerView() {
        searchAdapter = SearchResultAdapter { recipe ->
            val intent = Intent(requireContext(), RecipesDetailActivity::class.java)
            intent.putExtra(HomeFragment.DISCOVER_RECIPE_EXTRA, recipe.toRecipesItem())
            startActivity(intent)
        }

        binding.rvingredients.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchAdapter
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.loadingLayout.root.visibility = if (isLoading) View.VISIBLE else View.GONE
                if (isLoading) {
                    binding.emptyState.root.visibility = View.GONE
                    binding.rvingredients.visibility = View.GONE
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchResults.collect { result ->
                result?.fold(
                    onSuccess = { response ->
                        if (response.success == true) {
                            if (response.data.isNullOrEmpty()) {
                                showEmptyState(
                                    "Resep Tidak Ditemukan", 
                                    "Coba kombinasi bahan yang berbeda"
                                )
                            } else {
                                hideEmptyState()
                                response.data.filterNotNull().let { recipes ->
                                    searchAdapter.submitList(recipes)
                                }
                            }
                        } else {
                            showError("Gagal mendapatkan resep")
                        }
                    },
                    onFailure = { e ->
                        handleSearchError(e)
                    }
                )
            }
        }
    }

    private fun showEmptyState(title: String, description: String) {
        binding.apply {
            rvingredients.visibility = View.GONE
            emptyState.apply {
                root.visibility = View.VISIBLE
                emptyText.text = title
                emptyDescription.text = description
            }
        }
    }

    private fun hideEmptyState() {
        binding.apply {
            rvingredients.visibility = View.VISIBLE
            emptyState.root.visibility = View.GONE
        }
    }

    private fun handleSearchError(e: Throwable) {
        when {
            e is java.net.UnknownHostException -> {
                showEmptyState(
                    "No internet connection",
                    "Please check your internet connection and try again"
                )
            }
            e is java.net.SocketTimeoutException -> {
                showEmptyState(
                    "Connection timeout",
                    "Please try again"
                )
            }
            else -> {
                showEmptyState(
                    "Something went wrong",
                    "Please try again later"
                )
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        showEmptyState(
            "Oops!",
            message
        )
    }

    private fun showInitialEmptyState() {
        binding.apply {
            rvingredients.visibility = View.GONE
            emptyState.apply {
                root.visibility = View.VISIBLE
                emptyText.text = "Pilih Bahan Makanan"
                emptyDescription.text = "Pilih bahan makanan yang kamu punya, lalu tekan tombol search untuk menemukan resep"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}