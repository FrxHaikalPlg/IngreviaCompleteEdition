package com.frxhaikal_plg.ingrevia.data.remote.api

import com.frxhaikal_plg.ingrevia.data.remote.model.RecomendationsResponse
import com.frxhaikal_plg.ingrevia.data.remote.model.RecommendationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MLApiService {
    @POST("api/recommend")
    suspend fun getRecommendations(@Body request: RecommendationRequest): Response<RecomendationsResponse>
} 