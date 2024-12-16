package com.frxhaikal_plg.ingrevia.data.remote.api

object ApiConfig {
    fun getApiService(): ApiService {
        val retrofit = RetrofitClient.getInstance()
        return retrofit.create(ApiService::class.java)
    }
} 