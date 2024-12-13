package com.frxhaikal_plg.ingrevia.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frxhaikal_plg.ingrevia.data.network.NetworkResult
import com.frxhaikal_plg.ingrevia.data.remote.model.auth.RegisterResponse
import com.frxhaikal_plg.ingrevia.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _registerState = MutableStateFlow<NetworkResult<RegisterResponse>?>(null)
    val registerState: StateFlow<NetworkResult<RegisterResponse>?> = _registerState

    fun register(email: String, password: String, name: String) {
        viewModelScope.launch {
            repository.register(email, password, name).collect { result ->
                _registerState.value = result
            }
        }
    }
} 