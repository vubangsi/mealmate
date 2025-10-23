package com.mercel.mealmate.domain.usecase

import com.mercel.mealmate.domain.model.Recipe
import com.mercel.mealmate.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecipeUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    operator fun invoke(id: String): Flow<Result<Recipe>> {
        return repository.getRecipe(id)
    }
}
