package com.mercel.mealmate.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercel.mealmate.domain.repository.AiRepository
import com.mercel.mealmate.domain.repository.MealPlanRepository
import com.mercel.mealmate.domain.repository.RecipeRepository
import com.mercel.mealmate.domain.repository.ShoppingRepository
import com.mercel.mealmate.presentation.home.uistate.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val mealPlanRepository: MealPlanRepository,
    private val shoppingRepository: ShoppingRepository,
    private val aiRepository: AiRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeStats()
    }

    private fun loadHomeStats() {
        viewModelScope.launch {
            // Load statistics for the home screen
            try {
                mealPlanRepository.getWeeklyPlan().collect { mealEntries ->
                    val plannedDays = mealEntries.map { it.dayOfWeek }.distinct().size
                    _uiState.update { it.copy(plannedDays = plannedDays) }
                }
            } catch (e: Exception) {
                // Handle error silently for stats
            }
            
            try {
                shoppingRepository.getShoppingList().collect { items ->
                    _uiState.update { it.copy(shoppingItems = items.size) }
                }
            } catch (e: Exception) {
                // Handle error silently for stats
            }
        }
    }

    fun scanFridge() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                shoppingRepository.scanAvailableIngredients()
                _uiState.update { it.copy(isLoading = false, message = "Fridge scanned successfully!") }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        error = "Failed to scan fridge: ${e.message}"
                    )
                }
            }
        }
    }

    fun getAiSuggestions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                // Get AI suggestions based on user's current meal plan and preferences
                val preferences = mapOf(
                    "type" to "daily_suggestions",
                    "focus" to "health_and_variety"
                )
                val result = aiRepository.generateWeeklyPlan(preferences)
                result.fold(
                    onSuccess = { suggestions ->
                        _uiState.update { 
                            it.copy(
                                isLoading = false, 
                                message = "AI suggestions ready!",
                                aiSuggestions = suggestions
                            )
                        }
                    },
                    onFailure = { error ->
                        _uiState.update { 
                            it.copy(
                                isLoading = false, 
                                error = "Failed to get AI suggestions: ${error.message}"
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        error = "Failed to get AI suggestions: ${e.message}"
                    )
                }
            }
        }
    }

    fun startMealPrep() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                // Generate a meal prep plan for the week
                val preferences = mapOf(
                    "type" to "meal_prep",
                    "duration" to "week",
                    "focus" to "efficiency"
                )
                val result = aiRepository.generateWeeklyPlan(preferences)
                result.fold(
                    onSuccess = { plan ->
                        _uiState.update { 
                            it.copy(
                                isLoading = false, 
                                message = "Meal prep plan generated!"
                            )
                        }
                    },
                    onFailure = { error ->
                        _uiState.update { 
                            it.copy(
                                isLoading = false, 
                                error = "Failed to generate meal prep plan: ${error.message}"
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        error = "Failed to start meal prep: ${e.message}"
                    )
                }
            }
        }
    }

    fun viewNutrition() {
        // TODO: Implement nutrition tracking
        _uiState.update { 
            it.copy(message = "Nutrition tracking coming soon!")
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(message = null, error = null) }
    }
}