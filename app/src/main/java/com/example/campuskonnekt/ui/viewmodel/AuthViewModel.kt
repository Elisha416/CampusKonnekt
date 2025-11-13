package com.example.campuskonnekt.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuskonnekt.data.repository.AuthRepository
import com.example.campuskonnekt.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null
)

class AuthViewModel(private val authRepository: AuthRepository = AuthRepository()) : ViewModel() {
    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        val currentUser = authRepository.getCurrentUser()
        _authState.update { it.copy(user = currentUser) }
    }

    fun login(email: String, password: String, onLoginSuccess: () -> Unit) {
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true, error = null) }
            val result = authRepository.login(email, password)
            result.onSuccess { user ->
                _authState.update { it.copy(isLoading = false, user = user) }
                onLoginSuccess()
            }.onFailure { e ->
                _authState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun register(
        email: String,
        password: String,
        displayName: String,
        major: String,
        year: String,
        onRegisterSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true, error = null) }
            val result = authRepository.register(email, password, displayName, major, year)
            result.onSuccess { user ->
                _authState.update { it.copy(isLoading = false, user = user) }
                onRegisterSuccess()
            }.onFailure { e ->
                _authState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun logout() {
        authRepository.logout()
        _authState.update { AuthState() } // Reset state
    }
}
