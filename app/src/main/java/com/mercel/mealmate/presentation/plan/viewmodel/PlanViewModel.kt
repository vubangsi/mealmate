package com.mercel.mealmate.presentation.plan.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercel.mealmate.domain.repository.MealPlanRepository
import com.mercel.mealmate.presentation.plan.uistate.PlanUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanViewModel @Inject constructor(
    private val mealPlanRepository: MealPlanRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlanUiState())
    val uiState: StateFlow<PlanUiState> = _uiState.asStateFlow()

    init {
        loadMealPlan()
    }

    private fun loadMealPlan() {
        viewModelScope.launch {
            mealPlanRepository.getWeeklyPlan().collect { entries ->
                _uiState.update { it.copy(mealEntries = entries, isLoading = false) }
            }
        }
    }

    fun generateWeeklyPlan() {
        viewModelScope.launch {
            _uiState.update { it.copy(isGenerating = true, error = null) }
            try {
                mealPlanRepository.generateWeeklyPlan(emptyMap())
                _uiState.update { it.copy(isGenerating = false, error = null) }
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Failed to generate meal plan. Please try again."
                _uiState.update {
                    it.copy(
                        isGenerating = false,
                        error = errorMessage
                    )
                }
            }
        }
    }

    fun removeMeal(mealEntryId: String) {
        viewModelScope.launch {
            mealPlanRepository.removeMealFromPlan(mealEntryId)
        }
    }

    fun clearPlan() {
        viewModelScope.launch {
            mealPlanRepository.clearWeeklyPlan()
        }
    }
}
