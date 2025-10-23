package com.mercel.mealmate.domain.repository

import com.mercel.mealmate.domain.model.AppTheme
import com.mercel.mealmate.domain.model.UserPrefs
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val userPrefs: Flow<UserPrefs>
    
    suspend fun updateDiet(diet: String?)
    
    suspend fun updateIntolerances(intolerances: Set<String>)
    
    suspend fun updateCaloriesPerDay(calories: Int?)
    
    suspend fun updateBudgetPerWeek(budget: Int?)
    
    suspend fun updateTheme(theme: AppTheme)
}
