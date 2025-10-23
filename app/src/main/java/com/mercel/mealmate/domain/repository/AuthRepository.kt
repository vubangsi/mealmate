package com.mercel.mealmate.domain.repository

import com.mercel.mealmate.domain.model.AuthState
import com.mercel.mealmate.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getAuthState(): Flow<AuthState>
    suspend fun login(email: String, password: String, rememberMe: Boolean): Result<User>
    suspend fun register(email: String, password: String, name: String): Result<User>
    suspend fun logout()
    suspend fun isLoggedIn(): Boolean
    suspend fun getCurrentUser(): User?
    suspend fun updateUser(user: User)
}