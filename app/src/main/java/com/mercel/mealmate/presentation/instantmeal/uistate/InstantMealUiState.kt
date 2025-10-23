package com.mercel.mealmate.presentation.instantmeal.uistate

data class InstantMealUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val message: String? = null,
    val availableIngredients: List<String> = emptyList(),
    val mealPlan: String? = null,
    val scanSource: String? = null // "fridge", "pantry", "both"
)