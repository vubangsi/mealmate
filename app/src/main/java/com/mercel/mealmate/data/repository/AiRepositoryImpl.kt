package com.mercel.mealmate.data.repository

import android.util.Log
import com.mercel.mealmate.data.remote.api.OpenAiApi
import com.mercel.mealmate.data.remote.dto.ChatMessage
import com.mercel.mealmate.data.remote.dto.OpenAiRequest
import com.mercel.mealmate.domain.repository.AiRepository
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import javax.inject.Named

class AiRepositoryImpl @Inject constructor(
    private val openAiApi: OpenAiApi,
    @Named("openai_api_key") private val apiKey: String
) : AiRepository {

    companion object {
        private const val TAG = "AiRepositoryImpl"
        private const val TIMEOUT_MS = 30000L // 30 seconds
        private const val MODEL = "gpt-3.5-turbo"
    }

    override suspend fun generateWeeklyPlan(preferences: Map<String, Any>): Result<String> {
        return try {
            val prompt = buildString {
                append("Generate a weekly meal plan with the following preferences:\n")
                preferences.forEach { (key, value) ->
                    append("- $key: $value\n")
                }
                append("\nProvide a balanced meal plan for breakfast, lunch, and dinner for 7 days. ")
                append("Format the response as a clear weekly schedule.")
            }

            Log.d(TAG, "Generating weekly plan with OpenAI (timeout ${TIMEOUT_MS}ms)...")
            val response = withTimeoutOrNull(TIMEOUT_MS) {
                openAiApi.generateCompletion(
                    authHeader = "Bearer $apiKey",
                    request = OpenAiRequest(
                        model = MODEL,
                        messages = listOf(
                            ChatMessage(role = "system", content = "You are a helpful meal planning assistant."),
                            ChatMessage(role = "user", content = prompt)
                        ),
                        temperature = 0.7,
                        maxTokens = 2048
                    )
                )
            }

            if (response == null) {
                Log.e(TAG, "Weekly plan generation timed out after ${TIMEOUT_MS}ms")
                Result.failure(Exception("AI generation timed out. Please check your internet connection and try again."))
            } else {
                val result = response.choices.firstOrNull()?.message?.content ?: "No plan generated"
                Log.d(TAG, "Weekly plan generated successfully: ${result.length} chars")
                Result.success(result)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error generating weekly plan: ${e.message}", e)
            val errorMsg = when {
                e.message?.contains("API key", ignoreCase = true) == true ->
                    "Invalid API key. Please check your OpenAI API key configuration."
                e.message?.contains("network", ignoreCase = true) == true ->
                    "Network error. Please check your internet connection."
                e.message?.contains("timeout", ignoreCase = true) == true ->
                    "Request timed out. Please try again."
                e.message?.contains("401", ignoreCase = true) == true ->
                    "Unauthorized. Please check your OpenAI API key."
                e.message?.contains("429", ignoreCase = true) == true ->
                    "Rate limited. Please wait a moment and try again."
                else -> "Failed to generate meal plan: ${e.message}"
            }
            Result.failure(Exception(errorMsg))
        }
    }

    override suspend fun generateRecipeSummary(
        recipeTitle: String,
        ingredients: List<String>
    ): Result<String> {
        return try {
            val prompt = """
                Create a brief, engaging summary for this recipe:
                Title: $recipeTitle
                Ingredients: ${ingredients.joinToString(", ")}

                Provide a 2-3 sentence description highlighting key flavors and appeal.
            """.trimIndent()

            Log.d(TAG, "Generating recipe summary for: $recipeTitle")
            val response = withTimeoutOrNull(TIMEOUT_MS) {
                openAiApi.generateCompletion(
                    authHeader = "Bearer $apiKey",
                    request = OpenAiRequest(
                        model = MODEL,
                        messages = listOf(
                            ChatMessage(role = "system", content = "You are a helpful culinary assistant."),
                            ChatMessage(role = "user", content = prompt)
                        ),
                        temperature = 0.7,
                        maxTokens = 500
                    )
                )
            }

            if (response == null) {
                Log.e(TAG, "Recipe summary generation timed out")
                Result.failure(Exception("AI generation timed out. Please check your internet connection and try again."))
            } else {
                val result = response.choices.firstOrNull()?.message?.content ?: "No summary generated"
                Log.d(TAG, "Recipe summary generated successfully: ${result.length} chars")
                Result.success(result)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error generating recipe summary: ${e.message}", e)
            val errorMsg = when {
                e.message?.contains("API key", ignoreCase = true) == true ->
                    "Invalid API key. Please check your OpenAI API key configuration."
                e.message?.contains("network", ignoreCase = true) == true ->
                    "Network error. Please check your internet connection."
                e.message?.contains("timeout", ignoreCase = true) == true ->
                    "Request timed out. Please try again."
                e.message?.contains("401", ignoreCase = true) == true ->
                    "Unauthorized. Please check your OpenAI API key."
                e.message?.contains("429", ignoreCase = true) == true ->
                    "Rate limited. Please wait a moment and try again."
                else -> "Failed to generate summary: ${e.message}"
            }
            Result.failure(Exception(errorMsg))
        }
    }

    override suspend fun optimizeShoppingList(items: List<String>): Result<String> {
        return try {
            val prompt = """
                Optimize this shopping list by grouping items by store section:
                ${items.joinToString("\n")}

                Group items into: Produce, Dairy, Meat, Pantry, Frozen, Bakery, Other
            """.trimIndent()

            Log.d(TAG, "Optimizing shopping list with ${items.size} items")
            val response = withTimeoutOrNull(TIMEOUT_MS) {
                openAiApi.generateCompletion(
                    authHeader = "Bearer $apiKey",
                    request = OpenAiRequest(
                        model = MODEL,
                        messages = listOf(
                            ChatMessage(role = "system", content = "You are a helpful shopping assistant."),
                            ChatMessage(role = "user", content = prompt)
                        ),
                        temperature = 0.7,
                        maxTokens = 1000
                    )
                )
            }

            if (response == null) {
                Log.e(TAG, "Shopping list optimization timed out")
                Result.failure(Exception("AI generation timed out. Please check your internet connection and try again."))
            } else {
                val result = response.choices.firstOrNull()?.message?.content ?: "No optimization generated"
                Log.d(TAG, "Shopping list optimized successfully: ${result.length} chars")
                Result.success(result)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error optimizing shopping list: ${e.message}", e)
            val errorMsg = when {
                e.message?.contains("API key", ignoreCase = true) == true ->
                    "Invalid API key. Please check your OpenAI API key configuration."
                e.message?.contains("network", ignoreCase = true) == true ->
                    "Network error. Please check your internet connection."
                e.message?.contains("timeout", ignoreCase = true) == true ->
                    "Request timed out. Please try again."
                e.message?.contains("401", ignoreCase = true) == true ->
                    "Unauthorized. Please check your OpenAI API key."
                e.message?.contains("429", ignoreCase = true) == true ->
                    "Rate limited. Please wait a moment and try again."
                else -> "Failed to optimize shopping list: ${e.message}"
            }
            Result.failure(Exception(errorMsg))
        }
    }

    override suspend fun suggestSubstitution(ingredient: String, dietary: String?): Result<String> {
        return try {
            val prompt = if (dietary != null) {
                "Suggest a $dietary-friendly substitution for: $ingredient"
            } else {
                "Suggest a healthy substitution for: $ingredient"
            }

            Log.d(TAG, "Suggesting substitution for: $ingredient")
            val response = withTimeoutOrNull(TIMEOUT_MS) {
                openAiApi.generateCompletion(
                    authHeader = "Bearer $apiKey",
                    request = OpenAiRequest(
                        model = MODEL,
                        messages = listOf(
                            ChatMessage(role = "system", content = "You are a helpful nutrition assistant."),
                            ChatMessage(role = "user", content = prompt)
                        ),
                        temperature = 0.7,
                        maxTokens = 500
                    )
                )
            }

            if (response == null) {
                Log.e(TAG, "Substitution suggestion timed out")
                Result.failure(Exception("AI generation timed out. Please check your internet connection and try again."))
            } else {
                val result = response.choices.firstOrNull()?.message?.content ?: "No substitution found"
                Log.d(TAG, "Substitution suggested successfully: ${result.length} chars")
                Result.success(result)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error suggesting substitution: ${e.message}", e)
            val errorMsg = when {
                e.message?.contains("API key", ignoreCase = true) == true ->
                    "Invalid API key. Please check your OpenAI API key configuration."
                e.message?.contains("network", ignoreCase = true) == true ->
                    "Network error. Please check your internet connection."
                e.message?.contains("timeout", ignoreCase = true) == true ->
                    "Request timed out. Please try again."
                e.message?.contains("401", ignoreCase = true) == true ->
                    "Unauthorized. Please check your OpenAI API key."
                e.message?.contains("429", ignoreCase = true) == true ->
                    "Rate limited. Please wait a moment and try again."
                else -> "Failed to suggest substitution: ${e.message}"
            }
            Result.failure(Exception(errorMsg))
        }
    }

    override suspend fun optimizeShoppingList(items: List<String>, customPrompt: String): Result<String> {
        return try {
            Log.d(TAG, "Optimizing shopping list with custom prompt (timeout ${TIMEOUT_MS}ms)...")
            val response = withTimeoutOrNull(TIMEOUT_MS) {
                openAiApi.generateCompletion(
                    authHeader = "Bearer $apiKey",
                    request = OpenAiRequest(
                        model = MODEL,
                        messages = listOf(
                            ChatMessage(role = "system", content = "You are an expert grocery shopping optimizer and meal planning assistant."),
                            ChatMessage(role = "user", content = customPrompt)
                        ),
                        temperature = 0.3,
                        maxTokens = 2048
                    )
                )
            }

            if (response == null) {
                Log.e(TAG, "Shopping list optimization timed out after ${TIMEOUT_MS}ms")
                return Result.failure(Exception("Request timed out after ${TIMEOUT_MS / 1000} seconds"))
            }

            Log.d(TAG, "Shopping list optimization successful")
            Result.success(response.choices.firstOrNull()?.message?.content ?: "No optimization suggestions available")

        } catch (e: Exception) {
            Log.e(TAG, "Error optimizing shopping list: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun analyzeAvailableIngredients(prompt: String): Result<String> {
        return try {
            Log.d(TAG, "Analyzing available ingredients (timeout ${TIMEOUT_MS}ms)...")
            val response = withTimeoutOrNull(TIMEOUT_MS) {
                openAiApi.generateCompletion(
                    authHeader = "Bearer $apiKey",
                    request = OpenAiRequest(
                        model = MODEL,
                        messages = listOf(
                            ChatMessage(role = "system", content = "You are an expert food recognition and inventory management assistant. Analyze images of fridges and pantries to identify available ingredients."),
                            ChatMessage(role = "user", content = prompt)
                        ),
                        temperature = 0.2,
                        maxTokens = 1024
                    )
                )
            }

            if (response == null) {
                Log.e(TAG, "Ingredient analysis timed out after ${TIMEOUT_MS}ms")
                return Result.failure(Exception("Request timed out after ${TIMEOUT_MS / 1000} seconds"))
            }

            Log.d(TAG, "Ingredient analysis successful")
            Result.success(response.choices.firstOrNull()?.message?.content ?: "No ingredients identified")

        } catch (e: Exception) {
            Log.e(TAG, "Error analyzing available ingredients: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun generateInstantMealPlan(availableIngredients: List<String>): Result<String> {
        return try {
            val prompt = """
            Create an instant meal plan for today (breakfast, lunch, dinner) using ONLY these available ingredients:
            
            AVAILABLE INGREDIENTS:
            ${availableIngredients.joinToString("\n")}
            
            REQUIREMENTS:
            - Use only ingredients from the list above
            - Create balanced, nutritious meals
            - Provide quick, simple recipes (15-30 minutes max)
            - Suggest substitutions if certain ingredients are missing
            - Include cooking instructions and estimated prep/cook times
            
            FORMAT:
            BREAKFAST:
            - Recipe Name
            - Ingredients used: [list]
            - Instructions: [brief steps]
            - Prep time: X minutes
            
            LUNCH:
            [same format]
            
            DINNER:
            [same format]
            
            SHOPPING NOTES:
            [Any critical missing ingredients needed for better meals]
            """

            Log.d(TAG, "Generating instant meal plan from ${availableIngredients.size} ingredients (timeout ${TIMEOUT_MS}ms)...")
            val response = withTimeoutOrNull(TIMEOUT_MS) {
                openAiApi.generateCompletion(
                    authHeader = "Bearer $apiKey",
                    request = OpenAiRequest(
                        model = MODEL,
                        messages = listOf(
                            ChatMessage(role = "system", content = "You are an expert chef and meal planning assistant. Create practical, delicious meals from available ingredients."),
                            ChatMessage(role = "user", content = prompt)
                        ),
                        temperature = 0.7,
                        maxTokens = 2048
                    )
                )
            }

            if (response == null) {
                Log.e(TAG, "Instant meal plan generation timed out after ${TIMEOUT_MS}ms")
                return Result.failure(Exception("Request timed out after ${TIMEOUT_MS / 1000} seconds"))
            }

            Log.d(TAG, "Instant meal plan generation successful")
            Result.success(response.choices.firstOrNull()?.message?.content ?: "Unable to generate meal plan")

        } catch (e: Exception) {
            Log.e(TAG, "Error generating instant meal plan: ${e.message}", e)
            Result.failure(e)
        }
    }
}
