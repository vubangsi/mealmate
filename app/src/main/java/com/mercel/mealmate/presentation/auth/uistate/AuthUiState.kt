package com.mercel.mealmate.presentation.auth.uistate

import com.mercel.mealmate.domain.model.User

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoginMode: Boolean = true,
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val rememberMe: Boolean = true,
    val user: User? = null,
    val isLoggedIn: Boolean = false,
    val errorMessage: String? = null
)