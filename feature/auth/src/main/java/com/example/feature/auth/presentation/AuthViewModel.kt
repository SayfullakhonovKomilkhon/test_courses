package com.example.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.common.Resource
import com.example.core.network.AuthResponse
import com.example.feature.auth.domain.usecase.LoginUseCase
import com.example.feature.auth.domain.usecase.RegisterUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<Resource<AuthResponse>?>(null)
    val authState: StateFlow<Resource<AuthResponse>?> = _authState.asStateFlow()

    private val _message = MutableSharedFlow<String>()
    val message: SharedFlow<String> = _message.asSharedFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = Resource.Loading
            val result = loginUseCase(email, password)
            _authState.value = result
            if (result is Resource.Error) {
                _message.emit(result.message)
            }
        }
    }

    fun register(email: String, password: String, repeatPw: String) {
        viewModelScope.launch {
            _authState.value = Resource.Loading
            val result = registerUseCase(email, password, repeatPw)
            _authState.value = result
            if (result is Resource.Error) {
                _message.emit(result.message)
            }
        }
    }

    fun clearState() {
        _authState.value = null
    }
}
