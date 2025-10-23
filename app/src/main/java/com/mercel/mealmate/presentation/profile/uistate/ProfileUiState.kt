package com.mercel.mealmate.presentation.profile.uistate

import com.mercel.mealmate.domain.model.User
import com.mercel.mealmate.domain.model.UserPrefs

data class ProfileUiState(
    val user: User? = null,
    val userPrefs: UserPrefs = UserPrefs(),
    val isLoading: Boolean = true
)