package com.frxhaikal_plg.ingrevia.data.remote.api

import com.frxhaikal_plg.ingrevia.data.remote.model.auth.LoginRequest
import com.frxhaikal_plg.ingrevia.data.remote.model.auth.LoginResponse
import com.frxhaikal_plg.ingrevia.data.remote.model.auth.ForgotPasswordResponse
import com.frxhaikal_plg.ingrevia.data.remote.model.auth.RegisterRequest
import com.frxhaikal_plg.ingrevia.data.remote.model.auth.RegisterResponse
import com.frxhaikal_plg.ingrevia.data.remote.model.home.DiscoverResponse
import com.frxhaikal_plg.ingrevia.data.remote.model.search.TitleSearchResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @POST("auth/login/email")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body email: Map<String, String>): Response<ForgotPasswordResponse>

    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @GET("home/discover")
    suspend fun getDiscoverRecipes(@Query("page") page: Int = 1, @Query("limit") limit: Int = 50): Response<DiscoverResponse>

    @GET("/home/search/title")
    suspend fun searchRecipesByTitle(@Query("q") query: String): Response<TitleSearchResponse>
} 