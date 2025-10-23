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

    // Removed dummy implementations of scanFridge, getAiSuggestions, startMealPrep, and viewNutrition
    // These tiles have been hidden from the UI as they only showed loading spinners without useful functionality

    fun clearMessage() {
        _uiState.update { it.copy(message = null, error = null) }
    }
}