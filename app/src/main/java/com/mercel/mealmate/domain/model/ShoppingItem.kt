package com.mercel.mealmate.domain.model

data class ShoppingItem(
    val id: String,
    val ingredientName: String,
    val quantity: String,
    val section: String? = null,
    val estimatedPrice: Double? = null,
    val notes: String? = null,
    val checked: Boolean = false,
    val isAvailableAtHome: Boolean = false
)
