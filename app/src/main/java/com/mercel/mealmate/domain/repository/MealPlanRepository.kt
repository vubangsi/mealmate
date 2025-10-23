package com.mercel.mealmate.domain.repository

import com.mercel.mealmate.domain.model.MealEntry
import com.mercel.mealmate.domain.model.MealSlot
import kotlinx.coroutines.flow.Flow

interface MealPlanRepository {
    fun getWeeklyPlan(): Flow<List<MealEntry>>
    
    suspend fun addMealToPlan(recipeId: String, dayOfWeek: Int, slot: MealSlot)
    
    suspend fun removeMealFromPlan(mealEntryId: String)
    
    suspend fun clearWeeklyPlan()
    
    suspend fun generateWeeklyPlan(preferences: Map<String, Any>)
}
