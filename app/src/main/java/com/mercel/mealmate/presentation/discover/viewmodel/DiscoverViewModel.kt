package com.mercel.mealmate.presentation.discover.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercel.mealmate.domain.usecase.SearchRecipesUseCase
import com.mercel.mealmate.domain.usecase.ToggleFavoriteUseCase
import com.mercel.mealmate.presentation.discover.uistate.DiscoverUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val searchRecipesUseCase: SearchRecipesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiscoverUiState(searchQuery = "chicken"))
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

    init {
        searchRecipes()
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onDietChange(diet: String?) {
        _uiState.update { it.copy(selectedDiet = diet) }
        searchRecipes()
    }

    fun onIntolerancesChange(intolerances: Set<String>) {
        _uiState.update { it.copy(selectedIntolerances = intolerances) }
        searchRecipes()
    }

    fun searchRecipes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            searchRecipesUseCase(
                query = _uiState.value.searchQuery,
                diet = _uiState.value.selectedDiet,
                intolerances = _uiState.value.selectedIntolerances
            ).collect { result ->
                result.fold(
                    onSuccess = { recipes ->
                        _uiState.update {
                            it.copy(
                                recipes = recipes,
                                isLoading = false,
                                error = null
                            )
                        }
                    },
                    onFailure = { error ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = error.message ?: "Unknown error occurred"
                            )
                        }
                    }
                )
            }
        }
    }

    fun toggleFavorite(recipeId: String) {
        viewModelScope.launch {
            toggleFavoriteUseCase(recipeId)
        }
    }
}
