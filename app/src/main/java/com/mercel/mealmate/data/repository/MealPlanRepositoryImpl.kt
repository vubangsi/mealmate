package com.mercel.mealmate.data.repository

import android.util.Log
import com.mercel.mealmate.data.local.dao.MealPlanDao
import com.mercel.mealmate.data.local.dao.RecipeDao
import com.mercel.mealmate.data.local.entity.MealEntryEntity
import com.mercel.mealmate.data.mapper.toDomain
import com.mercel.mealmate.domain.model.MealEntry
import com.mercel.mealmate.domain.model.MealSlot
import com.mercel.mealmate.domain.repository.AiRepository
import com.mercel.mealmate.domain.repository.MealPlanRepository
import com.mercel.mealmate.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class MealPlanRepositoryImpl @Inject constructor(
    private val mealPlanDao: MealPlanDao,
    private val recipeDao: RecipeDao,
    private val aiRepository: AiRepository,
    private val recipeRepository: RecipeRepository
) : MealPlanRepository {
    
    companion object {
        private const val TAG = "MealPlanRepositoryImpl"
    }

    override fun getWeeklyPlan(): Flow<List<MealEntry>> {
        return mealPlanDao.getAllMealEntries().map { entries ->
            entries.map { it.toDomain() }
        }
    }

    override suspend fun addMealToPlan(recipeId: String, dayOfWeek: Int, slot: MealSlot) {
        Log.d(TAG, "Adding recipe $recipeId to meal plan")
        
        // Check if we have complete recipe data with ingredients
        var recipe = recipeDao.getRecipeById(recipeId).first()
        
        // If recipe doesn't exist or has no ingredients, fetch from API
        if (recipe == null || recipe.ingredients.isEmpty()) {
            Log.d(TAG, "Recipe $recipeId has no ingredients, fetching from API")
            
            // Fetch complete recipe data from API
            recipeRepository.getRecipe(recipeId).first().fold(
                onSuccess = { fetchedRecipe ->
                    Log.d(TAG, "Successfully fetched recipe with ${fetchedRecipe.ingredients.size} ingredients")
                    recipe = recipeDao.getRecipeById(recipeId).first()
                },
                onFailure = { error ->
                    Log.e(TAG, "Failed to fetch recipe details: ${error.message}")
                }
            )
        }
        
        recipe = recipeDao.getRecipeById(recipeId).first()
        Log.d(TAG, "Recipe ${recipe?.title} has ${recipe?.ingredients?.size ?: 0} ingredients")
        
        val entry = MealEntryEntity(
            id = UUID.randomUUID().toString(),
            recipeId = recipeId,
            dayOfWeek = dayOfWeek,
            slot = slot,
            recipeName = recipe?.title ?: "",
            recipeImageUrl = recipe?.imageUrl
        )
        mealPlanDao.insertMealEntry(entry)
    }

    override suspend fun removeMealFromPlan(mealEntryId: String) {
        mealPlanDao.deleteMealEntry(mealEntryId)
    }

    override suspend fun clearWeeklyPlan() {
        mealPlanDao.deleteAll()
    }

    override suspend fun generateWeeklyPlan(preferences: Map<String, Any>) {
        // Use AI to generate plan
        val result = aiRepository.generateWeeklyPlan(preferences)

        result.fold(
            onSuccess = { aiPlan ->
                Log.d(TAG, "AI plan generation successful, creating meal plan from available recipes")
                val recipes = recipeDao.getAllRecipes().first()
                if (recipes.isNotEmpty()) {
                    mealPlanDao.deleteAll()

                    val entries = mutableListOf<MealEntryEntity>()
                    for (day in 1..7) {
                        MealSlot.values().forEach { slot ->
                            val randomRecipe = recipes.random()
                            Log.d(TAG, "Adding recipe ${randomRecipe.title} (${randomRecipe.id}) to meal plan for day $day, slot $slot")
                            
                            // Ensure this recipe has complete ingredient data by calling addMealToPlan
                            // which will fetch from API if needed
                            val entry = MealEntryEntity(
                                id = UUID.randomUUID().toString(),
                                recipeId = randomRecipe.id,
                                dayOfWeek = day,
                                slot = slot,
                                recipeName = randomRecipe.title,
                                recipeImageUrl = randomRecipe.imageUrl
                            )
                            entries.add(entry)
                        }
                    }
                    
                    // Insert all entries first
                    mealPlanDao.insertMealEntries(entries)
                    
                    // Now ensure all recipes have complete ingredient data
                    Log.d(TAG, "Fetching complete ingredient data for all recipes in meal plan")
                    entries.forEach { entry ->
                        // Check if recipe has ingredients, if not fetch from API
                        val recipe = recipeDao.getRecipeById(entry.recipeId).first()
                        if (recipe == null || recipe.ingredients.isEmpty()) {
                            Log.d(TAG, "Recipe ${entry.recipeId} has no ingredients, fetching from API")
                            try {
                                recipeRepository.getRecipe(entry.recipeId).first().fold(
                                    onSuccess = { fetchedRecipe ->
                                        Log.d(TAG, "Successfully fetched ${fetchedRecipe.title} with ${fetchedRecipe.ingredients.size} ingredients")
                                    },
                                    onFailure = { error ->
                                        Log.e(TAG, "Failed to fetch recipe ${entry.recipeId}: ${error.message}")
                                    }
                                )
                            } catch (e: Exception) {
                                Log.e(TAG, "Exception fetching recipe ${entry.recipeId}: ${e.message}")
                            }
                        } else {
                            Log.d(TAG, "Recipe ${recipe.title} already has ${recipe.ingredients.size} ingredients")
                        }
                    }
                    
                } else {
                    throw Exception("No recipes available to generate meal plan. Please search for recipes first.")
                }
            },
            onFailure = { exception ->
                throw Exception("Failed to generate meal plan: ${exception.message}", exception)
            }
        )
    }
}
