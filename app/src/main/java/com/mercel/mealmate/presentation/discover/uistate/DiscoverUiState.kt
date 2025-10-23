package com.mercel.mealmate.presentation.discover.uistate

import com.mercel.mealmate.domain.model.Recipe

data class DiscoverUiState(
    val recipes: List<Recipe> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val selectedDiet: String? = null,
    val selectedIntolerances: Set<String> = emptySet()
)
