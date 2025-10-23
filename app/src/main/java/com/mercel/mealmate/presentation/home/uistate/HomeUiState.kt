package com.mercel.mealmate.presentation.home.uistate

data class HomeUiState(
    val totalRecipes: Int = 0,
    val plannedDays: Int = 0,
    val shoppingItems: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val message: String? = null,
    val aiSuggestions: String? = null
)