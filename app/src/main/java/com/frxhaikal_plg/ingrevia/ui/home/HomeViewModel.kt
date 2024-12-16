package com.frxhaikal_plg.ingrevia.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frxhaikal_plg.ingrevia.data.remote.model.RecomendationsResponse
import com.frxhaikal_plg.ingrevia.data.remote.model.RecommendationRequest
import com.frxhaikal_plg.ingrevia.data.remote.api.MLApiConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _recommendations = MutableStateFlow<Result<RecomendationsResponse>?>(null)
    val recommendations: StateFlow<Result<RecomendationsResponse>?> = _recommendations

    fun getRecommendations(request: RecommendationRequest) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = MLApiConfig.getApiService().getRecommendations(request)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _recommendations.value = Result.success(it)
                    } ?: run {
                        _recommendations.value = Result.failure(Exception("Response body is null"))
                    }
                } else {
                    _recommendations.value = Result.failure(Exception("Error: ${response.code()}"))
                }
            } catch (e: Exception) {
                _recommendations.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}