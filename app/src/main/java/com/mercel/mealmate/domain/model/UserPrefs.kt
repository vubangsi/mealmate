package com.mercel.mealmate.domain.model

data class UserPrefs(
    val diet: String? = null,
    val intolerances: Set<String> = emptySet(),
    val caloriesPerDay: Int? = null,
    val budgetPerWeek: Int? = null,
    val theme: AppTheme = AppTheme.SYSTEM
)

enum class AppTheme {
    SYSTEM,
    LIGHT,
    DARK
}
