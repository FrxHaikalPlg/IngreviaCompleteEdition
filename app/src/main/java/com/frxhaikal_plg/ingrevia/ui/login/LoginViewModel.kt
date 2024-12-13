package com.frxhaikal_plg.ingrevia.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frxhaikal_plg.ingrevia.data.network.NetworkResult
import com.frxhaikal_plg.ingrevia.data.remote.model.auth.LoginResponse
import com.frxhaikal_plg.ingrevia.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _loginState = MutableStateFlow<NetworkResult<LoginResponse>?>(null)
    val loginState: StateFlow<NetworkResult<LoginResponse>?> = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            repository.login(email, password).collect { result ->
                _loginState.value = result
                if (result is NetworkResult.Success) {
                    result.data.data.let { loginData ->
                        repository.saveUserSession(
                            token = loginData.token,
                            userId = loginData.user.userId,
                            email = loginData.user.email,
                            displayName = loginData.user.displayName
                        )
                    }
                }
            }
        }
    }
} 