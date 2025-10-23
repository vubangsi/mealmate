package com.mercel.mealmate.presentation.instantmeal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercel.mealmate.domain.repository.AiRepository
import com.mercel.mealmate.domain.repository.MealPlanRepository
import com.mercel.mealmate.presentation.instantmeal.uistate.InstantMealUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InstantMealViewModel @Inject constructor(
    private val aiRepository: AiRepository,
    private val mealPlanRepository: MealPlanRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InstantMealUiState())
    val uiState: StateFlow<InstantMealUiState> = _uiState.asStateFlow()

    fun scanFridge() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            // Simulate fridge scanning - in real app this would use camera
            val fridgePrompt = """
            Analyze the uploaded fridge image and identify all visible food ingredients and items.
            
            Focus on identifying:
            - Fresh produce (vegetables, fruits)
            - Dairy products (milk, cheese, yogurt, eggs)
            - Leftovers and cooked items
            - Beverages and condiments
            - Any packaged foods with visible labels
            
            For each item, estimate:
            - Quantity available
            - Freshness/expiration status
            - Suitability for immediate cooking
            
            Return a simple list of available ingredients suitable for meal planning.
            """
            
            try {
                val result = aiRepository.analyzeAvailableIngredients(fridgePrompt)
                result.fold(
                    onSuccess = { analysis ->
                        val ingredients = parseIngredients(analysis)
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                availableIngredients = ingredients,
                                scanSource = "fridge"
                            )
                        }
                    },
                    onFailure = { error ->
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                error = "Failed to scan fridge: ${error.message}"
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = "Error scanning fridge: ${e.message}"
                    )
                }
            }
        }
    }

    fun scanPantry() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            val pantryPrompt = """
            Analyze the uploaded pantry/shelf image and identify all visible food ingredients and items.
            
            Focus on identifying:
            - Canned goods and preserves
            - Dry goods (rice, pasta, flour, cereals)
            - Spices and seasonings
            - Oils and vinegars
            - Baking ingredients
            - Snacks and packaged foods
            
            For each item, note:
            - Product name and brand if visible
            - Package size or quantity
            - Expiration dates if visible
            - Storage condition
            
            Return a comprehensive list of pantry ingredients available for cooking.
            """
            
            try {
                val result = aiRepository.analyzeAvailableIngredients(pantryPrompt)
                result.fold(
                    onSuccess = { analysis ->
                        val ingredients = parseIngredients(analysis)
                        val currentIngredients = _uiState.value.availableIngredients
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                availableIngredients = (currentIngredients + ingredients).distinct(),
                                scanSource = if (currentIngredients.isNotEmpty()) "both" else "pantry"
                            )
                        }
                    },
                    onFailure = { error ->
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                error = "Failed to scan pantry: ${error.message}"
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = "Error scanning pantry: ${e.message}"
                    )
                }
            }
        }
    }

    fun generateInstantMealPlan() {
        viewModelScope.launch {
            val availableIngredients = _uiState.value.availableIngredients
            if (availableIngredients.isEmpty()) {
                _uiState.update { 
                    it.copy(error = "Please scan your fridge or pantry first to identify available ingredients.")
                }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                val result = aiRepository.generateInstantMealPlan(availableIngredients)
                result.fold(
                    onSuccess = { mealPlan ->
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                mealPlan = mealPlan
                            )
                        }
                    },
                    onFailure = { error ->
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                error = "Failed to generate meal plan: ${error.message}"
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = "Error generating meal plan: ${e.message}"
                    )
                }
            }
        }
    }

    fun saveToMealPlan() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                // In a real implementation, this would parse the AI-generated meal plan
                // and add specific recipes to the user's meal plan
                // For now, we'll simulate success
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        message = "Meal plan saved successfully!"
                    )
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = "Failed to save meal plan: ${e.message}"
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null, message = null) }
    }

    private fun parseIngredients(analysis: String): List<String> {
        // Simple parsing of AI response to extract ingredient names
        return analysis.lines()
            .filter { line ->
                line.trim().isNotEmpty() && 
                (line.contains("-") || line.contains("•") || line.contains("|"))
            }
            .map { line ->
                line.replace(Regex("^[-•*\\d\\.\\s]*"), "") // Remove bullet points and numbers
                    .split("|")[0] // Take first part if pipe separated
                    .split("(")[0] // Remove quantity in parentheses
                    .trim()
            }
            .filter { it.isNotEmpty() }
            .distinct()
            .take(20) // Limit to reasonable number
    }
}