package com.mercel.mealmate.core.util

object Constants {
    const val DATABASE_NAME = "mealmate_database"
    const val DATASTORE_NAME = "mealmate_preferences"
    const val BASE_URL = "https://api.spoonacular.com/"
    
    // API Key should be stored in local.properties
    const val API_KEY_PROPERTY = "SPOONACULAR_API_KEY"
    const val GEMINI_API_KEY_PROPERTY = "GEMINI_API_KEY"
    
    // WorkManager
    const val WEEKLY_REMINDER_WORK = "weekly_reminder_work"
    const val SYNC_WORK = "sync_work"
    
    // Cache
    const val CACHE_EXPIRY_DAYS = 7L
    
    // Diets
    val DIET_OPTIONS = listOf(
        "Gluten Free",
        "Ketogenic",
        "Vegetarian",
        "Lacto-Vegetarian",
        "Ovo-Vegetarian",
        "Vegan",
        "Pescetarian",
        "Paleo",
        "Primal",
        "Low FODMAP",
        "Whole30"
    )
    
    // Intolerances
    val INTOLERANCE_OPTIONS = listOf(
        "Dairy",
        "Egg",
        "Gluten",
        "Grain",
        "Peanut",
        "Seafood",
        "Sesame",
        "Shellfish",
        "Soy",
        "Sulfite",
        "Tree Nut",
        "Wheat"
    )
}
