package com.mercel.mealmate.domain.usecase

import com.mercel.mealmate.domain.model.Recipe
import com.mercel.mealmate.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRecipesUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    operator fun invoke(
        query: String = "",
        diet: String? = null,
        intolerances: Set<String> = emptySet()
    ): Flow<Result<List<Recipe>>> {
        return repository.searchRecipes(query, diet, intolerances)
    }
}
