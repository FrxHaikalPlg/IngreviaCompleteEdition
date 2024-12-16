package com.frxhaikal_plg.ingrevia.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frxhaikal_plg.ingrevia.data.remote.api.ApiConfig
import com.frxhaikal_plg.ingrevia.data.remote.model.search.TitleSearchResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val _searchResults = MutableStateFlow<Result<TitleSearchResponse>?>(null)
    val searchResults: StateFlow<Result<TitleSearchResponse>?> = _searchResults

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun searchRecipes(query: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = ApiConfig.getApiService().searchRecipesByTitle(query)
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