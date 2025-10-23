package com.mercel.mealmate.domain.usecase.auth

import com.mercel.mealmate.domain.model.User
import com.mercel.mealmate.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String, rememberMe: Boolean): Result<User> {
        if (email.isBlank()) {
            return Result.failure(Exception("Email cannot be empty"))
        }
        if (password.isBlank()) {
            return Result.failure(Exception("Password cannot be empty"))
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Result.failure(Exception("Please enter a valid email address"))
        }
        
        return authRepository.login(email, password, rememberMe)
    }
}