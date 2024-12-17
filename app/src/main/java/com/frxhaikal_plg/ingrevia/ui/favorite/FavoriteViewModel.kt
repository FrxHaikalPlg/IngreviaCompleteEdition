package com.frxhaikal_plg.ingrevia.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frxhaikal_plg.ingrevia.data.remote.api.ApiConfig
import com.frxhaikal_plg.ingrevia.data.remote.model.favorite.AddFavoriteRequest
import com.frxhaikal_plg.ingrevia.data.remote.model.favorite.GetFavoriteResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel : ViewModel() {

    private val _favorites = MutableStateFlow<Result<GetFavoriteResponse>?>(null)
    val favorites: StateFlow<Result<GetFavoriteResponse>?> = _favorites

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getFavorites(userId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = ApiConfig.getApiService().getFavorites(userId)
                if (response.isSuccessful) {
                    response.body()?.let { responseBody: GetFavoriteResponse ->
                        _favorites.value = Result.success(responseBody)
                    }
                } else {
                    _favorites.value = Result.failure(Exception("Error: ${response.code()}"))
                }
            } catch (e: Exception) {
                _favorites.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addFavorite(userId: String, recipeId: String) {
        viewModelScope.launch {
            try {
                val request = AddFavoriteRequest(userId, recipeId)
                val response = ApiConfig.getApiService().addFavorite(request)
                if (response.isSuccessful) {
                    getFavorites(userId)
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun deleteFavorite(userId: String, recipeId: String) {
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().deleteFavorite(userId, recipeId)
                if (response.isSuccessful) {
                    getFavorites(userId)
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}