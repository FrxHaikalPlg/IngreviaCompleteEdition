package com.frxhaikal_plg.ingrevia.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.frxhaikal_plg.ingrevia.data.local.UserPreferences
import com.frxhaikal_plg.ingrevia.databinding.FragmentFavoriteBinding
import com.frxhaikal_plg.ingrevia.ui.detailrecipes.RecipesDetailActivity
import com.frxhaikal_plg.ingrevia.ui.favorite.adapter.FavoriteAdapter
import com.frxhaikal_plg.ingrevia.ui.home.HomeFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: FavoriteViewModel by viewModels()
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var userPreferences: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        userPreferences = UserPreferences(requireContext())
        setupRecyclerView()
        observeFavorites()
        return binding.root
    }

    private fun setupRecyclerView() {
        favoriteAdapter = FavoriteAdapter(emptyList()) { recipe ->
            val intent = Intent(requireContext(), RecipesDetailActivity::class.java)
            intent.putExtra(HomeFragment.DISCOVER_RECIPE_EXTRA, recipe)
            startActivity(intent)
        }

        binding.rvfavorite.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoriteAdapter
        }
    }

    private fun observeFavorites() {
        viewLifecycleOwner.lifecycleScope.launch {
            userPreferences.userId.first()?.let { userId ->
                viewModel.getFavorites(userId)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.favorites.collect { result ->
                result?.fold(
                    onSuccess = { response ->
                        response.data?.filterNotNull()?.let { favorites ->
                            favoriteAdapter.updateData(favorites)
                        }
                    },
                    onFailure = {
                        // Handle error
                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh data ketika fragment kembali visible
        lifecycleScope.launch {
            userPreferences.userId.first()?.let { userId ->
                viewModel.getFavorites(userId)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}