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

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage

    private val _totalPages = MutableStateFlow(1)
    val totalPages: StateFlow<Int> = _totalPages

    init {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                
                // Get total pages first
                val initialResponse = ApiConfig.getApiService().getDiscoverRecipes(page = 1)
                if (initialResponse.isSuccessful) {
                    initialResponse.body()?.let { discoverResponse ->
                        _recipes.value = Result.success(discoverResponse)
                        discoverResponse.data?.pagination?.let { pagination ->
                            _totalPages.value = pagination.totalPages ?: 1
                        }
                    }
                }
            } catch (e: Exception) {
                _recipes.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getDiscoverRecipes(page: Int = 1) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = ApiConfig.getApiService().getDiscoverRecipes(page = page)
                
                if (response.isSuccessful) {
                    response.body()?.let { discoverResponse ->
                        _recipes.value = Result.success(discoverResponse)
                        discoverResponse.data?.pagination?.let { pagination ->
                            _totalPages.emit(pagination.totalPages ?: 1)
                            _currentPage.emit(page)
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

    fun goToFirstPage() {
        getDiscoverRecipes(1)
    }

    fun goToLastPage() {
        getDiscoverRecipes(_totalPages.value)
    }

    fun goToPage(page: Int) {
        if (page in 1.._totalPages.value) {
            getDiscoverRecipes(page)
        }
    }

    fun nextPage() {
        if (_currentPage.value < _totalPages.value) {
            getDiscoverRecipes(_currentPage.value + 1)
        }
    }

    fun prevPage() {
        if (_currentPage.value > 1) {
            getDiscoverRecipes(_currentPage.value - 1)
        }
    }
} 