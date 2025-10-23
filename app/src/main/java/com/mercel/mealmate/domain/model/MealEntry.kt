package com.mercel.mealmate.domain.model

data class MealEntry(
    val id: String,
    val recipeId: String,
    val dayOfWeek: Int, // 1 = Monday, 7 = Sunday
    val slot: MealSlot,
    val recipeName: String = "",
    val recipeImageUrl: String? = null
)

enum class MealSlot {
    BREAKFAST,
    LUNCH,
    DINNER
}
