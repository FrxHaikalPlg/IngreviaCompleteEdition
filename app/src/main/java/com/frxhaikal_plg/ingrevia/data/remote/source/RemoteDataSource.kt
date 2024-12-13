package com.frxhaikal_plg.ingrevia.data.remote.source

import com.frxhaikal_plg.ingrevia.data.remote.api.ApiService
import com.frxhaikal_plg.ingrevia.data.remote.model.auth.LoginRequest
import com.frxhaikal_plg.ingrevia.data.remote.model.auth.LoginResponse
import com.frxhaikal_plg.ingrevia.data.network.NetworkResult
import com.frxhaikal_plg.ingrevia.data.remote.model.auth.ForgotPasswordResponse
import com.frxhaikal_plg.ingrevia.data.remote.model.auth.RegisterRequest
import com.frxhaikal_plg.ingrevia.data.remote.model.auth.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject

class RemoteDataSource(private val apiService: ApiService) {
    suspend fun login(email: String, password: String): Flow<NetworkResult<LoginResponse>> = flow {
        emit(NetworkResult.Loading())
        try {
            val response = apiService.login(LoginRequest(email, password))
            when {
                response.isSuccessful -> {
                    response.body()?.let {
                        emit(NetworkResult.Success(it))
                    } ?: emit(NetworkResult.Error("Response body is empty"))
                }
                else -> {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        JSONObject(errorBody ?: "").getString("message")
                    } catch (e: Exception) {
                        "An error occurred"
                    }
                    emit(NetworkResult.Error(errorMessage, response.code()))
                }
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun forgotPassword(email: String): Flow<NetworkResult<ForgotPasswordResponse>> = flow {
        emit(NetworkResult.Loading())
        try {
            val response = apiService.forgotPassword(mapOf("email" to email))
            when {
                response.isSuccessful -> {
                    response.body()?.let {
                        emit(NetworkResult.Success(it))
                    } ?: emit(NetworkResult.Error("Response body is empty"))
                }
                else -> {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        JSONObject(errorBody ?: "").getString("message")
                    } catch (e: Exception) {
                        "An error occurred"
                    }
                    emit(NetworkResult.Error(errorMessage, response.code()))
                }
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun register(email: String, password: String, name: String): Flow<NetworkResult<RegisterResponse>> = flow {
        emit(NetworkResult.Loading())
        try {
            val response = apiService.register(RegisterRequest(email, password, name))
            when {
                response.isSuccessful -> {
                    response.body()?.let {
                        emit(NetworkResult.Success(it))
                    } ?: emit(NetworkResult.Error("Response body is empty"))
                }
                else -> {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        JSONObject(errorBody ?: "").getString("message")
                    } catch (e: Exception) {
                        "An error occurred"
                    }
                    emit(NetworkResult.Error(errorMessage, response.code()))
                }
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)
}
