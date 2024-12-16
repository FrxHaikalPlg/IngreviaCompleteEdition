package com.frxhaikal_plg.ingrevia.ui.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frxhaikal_plg.ingrevia.data.remote.api.ApiConfig
import com.frxhaikal_plg.ingrevia.data.remote.model.home.DiscoverResponse
import com.frxhaikal_plg.ingrevia.data.remote.model.home.RecipesItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SeeMorePopularViewModel : ViewModel() {
    private val _recipes = MutableStateFlow<Result<DiscoverResponse>?>(null)
    val recipes: StateFlow<Result<DiscoverResponse>?> = _recipes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var currentPage = 1
    private var isLastPage = false
    private val recipesList = mutableListOf<RecipesItem>()

    fun getDiscoverRecipes() {
        if (isLastPage) return
        
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = ApiConfig.getApiService().getDiscoverRecipes(page = currentPage)
                
                if (response.isSuccessful) {
                    response.body()?.let { discoverResponse ->
                        recipesList.addAll(discoverResponse.data?.recipes?.filterNotNull() ?: emptyList())
                        _recipes.value = Result.success(discoverResponse)
                        
                        // Check if this is the last page
                        discoverResponse.data?.pagination?.let { pagination ->
                            isLastPage = pagination.page == pagination.totalPages
                        }
                    }
                } else {
                    _recipes.value = Result.failure(Exception("Error: ${response.code()}"))
                }
            } catch (e: Exception) {
                _recipes.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadNextPage() {
        currentPage++
        getDiscoverRecipes()
    }
} 