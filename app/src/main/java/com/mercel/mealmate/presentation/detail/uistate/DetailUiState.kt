package com.mercel.mealmate.presentation.detail.uistate

import com.mercel.mealmate.domain.model.Recipe

data class DetailUiState(
    val recipe: Recipe? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val aiSummary: String? = null,
    val isGeneratingSummary: Boolean = false,
    val summaryError: String? = null
)
