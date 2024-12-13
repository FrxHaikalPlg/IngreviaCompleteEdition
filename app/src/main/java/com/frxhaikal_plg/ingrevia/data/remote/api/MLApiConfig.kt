package com.frxhaikal_plg.ingrevia.data.remote.api

object MLApiConfig {
    fun getApiService(): MLApiService {
        val retrofit = MLRetrofitClient.getInstance()
        return retrofit.create(MLApiService::class.java)
    }
} 