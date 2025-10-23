package com.mercel.mealmate.domain.model

data class Recipe(
    val id: String,
    val title: String,
    val imageUrl: String?,
    val servings: Int,
    val readyInMinutes: Int,
    val cuisines: List<String>,
    val diets: List<String>,
    val ingredients: List<Ingredient>,
    val steps: List<String>,
    val nutrients: Nutrients,
    val isFavorite: Boolean = false
)

data class Ingredient(
    val name: String,
    val quantity: String,
    val section: String? = null
)

data class Nutrients(
    val calories: Int,
    val protein: Double,
    val carbs: Double,
    val fat: Double
)
