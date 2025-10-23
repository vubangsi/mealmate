package com.mercel.mealmate.presentation.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercel.mealmate.domain.repository.AuthRepository
import com.mercel.mealmate.domain.usecase.auth.LoginUseCase
import com.mercel.mealmate.domain.usecase.auth.RegisterUseCase
import com.mercel.mealmate.presentation.auth.uistate.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.getAuthState().collect { authState ->
                _uiState.update { it.copy(
                    isLoggedIn = authState.isLoggedIn,
                    user = authState.user,
                    rememberMe = authState.rememberMe
                )}
            }
        }
    }

    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email, errorMessage = null) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    fun updateName(name: String) {
        _uiState.update { it.copy(name = name, errorMessage = null) }
    }

    fun toggleRememberMe() {
        _uiState.update { it.copy(rememberMe = !it.rememberMe) }
    }

    fun toggleMode() {
        _uiState.update { 
            it.copy(
                isLoginMode = !it.isLoginMode,
                errorMessage = null
            )
        }
    }

    fun login() {
        val currentState = _uiState.value
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            loginUseCase(currentState.email, currentState.password, currentState.rememberMe)
                .onSuccess { user ->
                    _uiState.update { it.copy(
                        isLoading = false,
                        user = user,
                        isLoggedIn = true
                    )}
                }
                .onFailure { error ->
                    _uiState.update { it.copy(
                        isLoading = false,
                        errorMessage = error.message
                    )}
                }
        }
    }

    fun register() {
        val currentState = _uiState.value
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            registerUseCase(currentState.email, currentState.password, currentState.name)
                .onSuccess { user ->
                    _uiState.update { it.copy(
                        isLoading = false,
                        user = user,
                        isLoggedIn = true
                    )}
                }
                .onFailure { error ->
                    _uiState.update { it.copy(
                        isLoading = false,
                        errorMessage = error.message
                    )}
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _uiState.update { it.copy(
                isLoggedIn = false,
                user = null,
                email = "",
                password = "",
                name = "",
                errorMessage = null
            )}
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}