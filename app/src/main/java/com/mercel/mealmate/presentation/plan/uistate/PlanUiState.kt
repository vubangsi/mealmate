package com.mercel.mealmate.presentation.plan.uistate

import com.mercel.mealmate.domain.model.MealEntry

data class PlanUiState(
    val mealEntries: List<MealEntry> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isGenerating: Boolean = false
)
