package com.mercel.mealmate.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mercel.mealmate.domain.model.MealSlot

@Entity(tableName = "meal_entries")
data class MealEntryEntity(
    @PrimaryKey
    val id: String,
    val recipeId: String,
    val dayOfWeek: Int,
    val slot: MealSlot,
    val recipeName: String = "",
    val recipeImageUrl: String? = null
)
