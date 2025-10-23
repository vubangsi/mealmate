package com.mercel.mealmate.data.mapper

import com.mercel.mealmate.data.local.entity.IngredientEntity
import com.mercel.mealmate.data.local.entity.NutrientsEntity
import com.mercel.mealmate.data.local.entity.RecipeEntity
import com.mercel.mealmate.data.remote.dto.RecipeDto
import com.mercel.mealmate.domain.model.Ingredient
import com.mercel.mealmate.domain.model.Nutrients
import com.mercel.mealmate.domain.model.Recipe

fun RecipeDto.toEntity(): RecipeEntity {
    val calories = nutrition?.nutrients?.find { it.name == "Calories" }?.amount?.toInt() ?: 0
    val protein = nutrition?.nutrients?.find { it.name == "Protein" }?.amount ?: 0.0
    val carbs = nutrition?.nutrients?.find { it.name == "Carbohydrates" }?.amount ?: 0.0
    val fat = nutrition?.nutrients?.find { it.name == "Fat" }?.amount ?: 0.0

    return RecipeEntity(
        id = id.toString(),
        title = title,
        imageUrl = image,
        servings = servings ?: 1,
        readyInMinutes = readyInMinutes ?: 0,
        cuisines = cuisines ?: emptyList(),
        diets = diets ?: emptyList(),
        ingredients = extendedIngredients?.map {
            IngredientEntity(
                name = it.name,
                quantity = it.original,
                section = it.aisle
            )
        } ?: emptyList(),
        steps = analyzedInstructions?.firstOrNull()?.steps?.map { it.step } ?: emptyList(),
        nutrients = NutrientsEntity(
            calories = calories,
            protein = protein,
            carbs = carbs,
            fat = fat
        )
    )
}

fun RecipeEntity.toDomain(): Recipe {
    return Recipe(
        id = id,
        title = title,
        imageUrl = imageUrl,
        servings = servings,
        readyInMinutes = readyInMinutes,
        cuisines = cuisines,
        diets = diets,
        ingredients = ingredients.map {
            Ingredient(
                name = it.name,
                quantity = it.quantity,
                section = it.section
            )
        },
        steps = steps,
        nutrients = Nutrients(
            calories = nutrients.calories,
            protein = nutrients.protein,
            carbs = nutrients.carbs,
            fat = nutrients.fat
        ),
        isFavorite = isFavorite
    )
}
