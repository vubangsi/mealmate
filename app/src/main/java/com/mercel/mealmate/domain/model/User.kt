package com.mercel.mealmate.domain.model

data class User(
    val id: String,
    val email: String,
    val name: String,
    val createdAt: Long = System.currentTimeMillis(),
    val preferences: UserPrefs = UserPrefs()
)

data class AuthState(
    val isLoggedIn: Boolean = false,
    val user: User? = null,
    val rememberMe: Boolean = false,
    val lastLoginTime: Long = 0L
)