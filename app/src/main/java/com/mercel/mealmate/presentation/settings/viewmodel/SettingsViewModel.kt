package com.mercel.mealmate.presentation.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercel.mealmate.domain.model.AppTheme
import com.mercel.mealmate.domain.repository.AuthRepository
import com.mercel.mealmate.domain.repository.PreferencesRepository
import com.mercel.mealmate.presentation.settings.uistate.SettingsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadPreferences()
    }

    private fun loadPreferences() {
        viewModelScope.launch {
            preferencesRepository.userPrefs.collect { prefs ->
                _uiState.update { it.copy(userPrefs = prefs) }
            }
        }
    }

    fun updateDiet(diet: String?) {
        viewModelScope.launch {
            preferencesRepository.updateDiet(diet)
        }
    }

    fun updateIntolerances(intolerances: Set<String>) {
        viewModelScope.launch {
            preferencesRepository.updateIntolerances(intolerances)
        }
    }

    fun updateCalories(calories: Int?) {
        viewModelScope.launch {
            preferencesRepository.updateCaloriesPerDay(calories)
        }
    }

    fun updateBudget(budget: Int?) {
        viewModelScope.launch {
            preferencesRepository.updateBudgetPerWeek(budget)
        }
    }

    fun updateTheme(theme: AppTheme) {
        viewModelScope.launch {
            preferencesRepository.updateTheme(theme)
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
