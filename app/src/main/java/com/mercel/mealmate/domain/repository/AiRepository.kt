package com.mercel.mealmate.domain.repository

interface AiRepository {
    suspend fun generateWeeklyPlan(preferences: Map<String, Any>): Result<String>
    
    suspend fun generateRecipeSummary(recipeTitle: String, ingredients: List<String>): Result<String>
    
    suspend fun optimizeShoppingList(items: List<String>): Result<String>
    
    suspend fun optimizeShoppingList(items: List<String>, customPrompt: String): Result<String>
    
    suspend fun suggestSubstitution(ingredient: String, dietary: String?): Result<String>
    
    suspend fun analyzeAvailableIngredients(prompt: String): Result<String>
    
    suspend fun generateInstantMealPlan(availableIngredients: List<String>): Result<String>
}
