package com.mercel.mealmate.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_items")
data class ShoppingItemEntity(
    @PrimaryKey
    val id: String,
    val ingredientName: String,
    val quantity: String,
    val section: String? = null,
    val estimatedPrice: Double? = null,
    val notes: String? = null,
    val checked: Boolean = false,
    val isAvailableAtHome: Boolean = false
)
