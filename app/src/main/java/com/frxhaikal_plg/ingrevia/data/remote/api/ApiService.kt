package com.frxhaikal_plg.ingrevia.data.remote.api

import com.frxhaikal_plg.ingrevia.data.remote.model.auth.LoginRequest
import com.frxhaikal_plg.ingrevia.data.remote.model.auth.LoginResponse
import com.frxhaikal_plg.ingrevia.data.remote.model.auth.ForgotPasswordResponse
import com.frxhaikal_plg.ingrevia.data.remote.model.auth.RegisterRequest
import com.frxhaikal_plg.ingrevia.data.remote.model.auth.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login/email")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body email: Map<String, String>): Response<ForgotPasswordResponse>

    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>
} 