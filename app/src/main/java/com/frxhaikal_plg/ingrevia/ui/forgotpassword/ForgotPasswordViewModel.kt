package com.frxhaikal_plg.ingrevia.ui.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frxhaikal_plg.ingrevia.data.network.NetworkResult
import com.frxhaikal_plg.ingrevia.data.remote.model.auth.ForgotPasswordResponse
import com.frxhaikal_plg.ingrevia.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _forgotPasswordState = MutableStateFlow<NetworkResult<ForgotPasswordResponse>?>(null)
    val forgotPasswordState: StateFlow<NetworkResult<ForgotPasswordResponse>?> = _forgotPasswordState

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            repository.forgotPassword(email).collect { result ->
                _forgotPasswordState.value = result
            }
        }
    }
} 