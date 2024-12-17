package com.frxhaikal_plg.ingrevia.ui.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frxhaikal_plg.ingrevia.data.remote.api.ApiConfig
import com.frxhaikal_plg.ingrevia.data.remote.model.search.TitleSearchResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipesViewModel : ViewModel() {
    private val _searchResults = MutableStateFlow<Result<TitleSearchResponse>?>(null)
    val searchResults: StateFlow<Result<TitleSearchResponse>?> = _searchResults

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun searchRecipesByIngredients(ingredients: List<String>) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val ingredientsString = ingredients.joinToString(",")
                val response = ApiConfig.getApiService().searchRecipesByIngredients(ingredientsString)
                
                if (response.isSuccessful) {
                    response.body()?.let {
                        _searchResults.value = Result.success(it)
                    } ?: run {
                        _searchResults.value = Result.failure(Exception("Response body is null"))
                    }
                } else {
                    _searchResults.value = Result.failure(Exception("Error: ${response.code()}"))
                }
            } catch (e: Exception) {
                _searchResults.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}