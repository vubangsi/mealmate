package com.mercel.mealmate.domain.repository

import com.mercel.mealmate.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    fun searchRecipes(
        query: String = "",
        diet: String? = null,
        intolerances: Set<String> = emptySet()
    ): Flow<Result<List<Recipe>>>
    
    fun getRecipe(id: String): Flow<Result<Recipe>>
    
    suspend fun toggleFavorite(recipeId: String)
    
    fun getFavoriteRecipes(): Flow<List<Recipe>>
    
}
