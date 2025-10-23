package com.mercel.mealmate.presentation.settings.uistate

import com.mercel.mealmate.domain.model.UserPrefs

data class SettingsUiState(
    val userPrefs: UserPrefs = UserPrefs(),
    val isLoading: Boolean = false
)
