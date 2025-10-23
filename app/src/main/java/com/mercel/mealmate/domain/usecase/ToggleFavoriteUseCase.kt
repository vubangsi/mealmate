package com.mercel.mealmate.domain.usecase

import com.mercel.mealmate.domain.repository.RecipeRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(recipeId: String) {
        repository.toggleFavorite(recipeId)
    }
}
