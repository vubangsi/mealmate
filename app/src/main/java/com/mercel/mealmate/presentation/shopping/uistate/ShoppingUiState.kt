package com.mercel.mealmate.presentation.shopping.uistate

import com.mercel.mealmate.domain.model.ShoppingItem

data class ShoppingUiState(
    val items: List<ShoppingItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
