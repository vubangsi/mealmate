package com.mercel.mealmate.data.repository

import android.util.Log
import com.mercel.mealmate.data.local.dao.RecipeDao
import com.mercel.mealmate.data.mapper.toDomain
import com.mercel.mealmate.data.mapper.toEntity
import com.mercel.mealmate.data.remote.api.SpoonacularApi
import com.mercel.mealmate.domain.model.Recipe
import com.mercel.mealmate.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val api: SpoonacularApi,
    private val dao: RecipeDao,
    private val apiKey: String
) : RecipeRepository {
    
    companion object {
        private const val TAG = "RecipeRepositoryImpl"
    }

    override fun searchRecipes(
        query: String,
        diet: String?,
        intolerances: Set<String>
    ): Flow<Result<List<Recipe>>> = flow {
        try {
            // Try to fetch from API
            val intolerancesString = if (intolerances.isNotEmpty()) {
                intolerances.joinToString(",")
            } else null

            val response = api.searchRecipes(
                query = query,
                diet = diet,
                intolerances = intolerancesString,
                apiKey = apiKey
            )

            val entities = response.results.map { it.toEntity() }
            dao.insertRecipes(entities)

            emit(Result.success(entities.map { it.toDomain() }))
        } catch (e: Exception) {
            // Fallback to cached data
            val cached = dao.getAllRecipes().firstOrNull() ?: emptyList()
            if (cached.isNotEmpty()) {
                emit(Result.success(cached.map { it.toDomain() }))
            } else {
                emit(Result.failure(e))
            }
        }
    }

    override fun getRecipe(id: String): Flow<Result<Recipe>> = flow {
        try {
            Log.d(TAG, "Fetching recipe details for ID: $id")
            // Always fetch from API to ensure we have complete recipe details including ingredients and instructions
            val response = api.getRecipeById(id.toInt(), includeNutrition = true, apiKey = apiKey)
            Log.d(TAG, "API response received for recipe $id: title=${response.title}")
            Log.d(TAG, "Recipe has ${response.extendedIngredients?.size ?: 0} ingredients")
            Log.d(TAG, "Recipe has ${response.analyzedInstructions?.firstOrNull()?.steps?.size ?: 0} instruction steps")
            
            val entity = response.toEntity()
            dao.insertRecipe(entity)
            val domainRecipe = entity.toDomain()
            
            Log.d(TAG, "Converted to domain: ${domainRecipe.ingredients.size} ingredients, ${domainRecipe.steps.size} steps")
            emit(Result.success(domainRecipe))
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch recipe $id from API: ${e.message}", e)
            
            // Check if it's a quota exceeded error
            if (e.message?.contains("402") == true || e.message?.contains("daily points limit") == true) {
                Log.w(TAG, "API quota exceeded, using cached data for recipe $id")
            }
            
            // Fallback to local cache if API fails
            val cached = dao.getRecipeById(id).firstOrNull()
            if (cached != null) {
                Log.d(TAG, "Using cached recipe for $id: ${cached.ingredients.size} ingredients, ${cached.steps.size} steps")
                emit(Result.success(cached.toDomain()))
            } else {
                Log.e(TAG, "No cached recipe found for $id, API quota may be exceeded")
                emit(Result.failure(Exception("Recipe details unavailable. API quota exceeded and no cached data available for recipe $id")))
            }
        }
    }

    override suspend fun toggleFavorite(recipeId: String) {
        val recipe = dao.getRecipeById(recipeId).firstOrNull()
        recipe?.let {
            dao.updateFavoriteStatus(recipeId, !it.isFavorite)
        }
    }

    override fun getFavoriteRecipes(): Flow<List<Recipe>> {
        return dao.getFavoriteRecipes().map { recipes ->
            recipes.map { it.toDomain() }
        }
    }
    
}
