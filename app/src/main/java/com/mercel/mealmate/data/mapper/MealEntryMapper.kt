package com.mercel.mealmate.data.mapper

import com.mercel.mealmate.data.local.entity.MealEntryEntity
import com.mercel.mealmate.domain.model.MealEntry

fun MealEntryEntity.toDomain(): MealEntry {
    return MealEntry(
        id = id,
        recipeId = recipeId,
        dayOfWeek = dayOfWeek,
        slot = slot,
        recipeName = recipeName,
        recipeImageUrl = recipeImageUrl
    )
}

fun MealEntry.toEntity(): MealEntryEntity {
    return MealEntryEntity(
        id = id,
        recipeId = recipeId,
        dayOfWeek = dayOfWeek,
        slot = slot,
        recipeName = recipeName,
        recipeImageUrl = recipeImageUrl
    )
}
