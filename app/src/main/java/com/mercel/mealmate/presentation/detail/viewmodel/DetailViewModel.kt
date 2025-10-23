package com.mercel.mealmate.presentation.detail.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercel.mealmate.domain.repository.AiRepository
import com.mercel.mealmate.domain.usecase.GetRecipeUseCase
import com.mercel.mealmate.domain.usecase.ToggleFavoriteUseCase
import com.mercel.mealmate.presentation.detail.uistate.DetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getRecipeUseCase: GetRecipeUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val aiRepository: AiRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private val recipeId: String = checkNotNull(savedStateHandle["recipeId"])

    init {
        loadRecipe()
    }

    private fun loadRecipe() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            getRecipeUseCase(recipeId).collect { result ->
                result.fold(
                    onSuccess = { recipe ->
                        _uiState.update {
                            it.copy(
                                recipe = recipe,
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

    fun toggleFavorite() {
        viewModelScope.launch {
            _uiState.value.recipe?.let { recipe ->
                toggleFavoriteUseCase(recipe.id)
            }
        }
    }

    fun generateSummary() {
        viewModelScope.launch {
            _uiState.value.recipe?.let { recipe ->
                _uiState.update { it.copy(isGeneratingSummary = true, summaryError = null) }

                val result = aiRepository.generateRecipeSummary(
                    recipeTitle = recipe.title,
                    ingredients = recipe.ingredients.map { it.name }
                )

                result.fold(
                    onSuccess = { summary ->
                        _uiState.update {
                            it.copy(
                                aiSummary = summary,
                                isGeneratingSummary = false,
                                summaryError = null
                            )
                        }
                    },
                    onFailure = { error ->
                        _uiState.update {
                            it.copy(
                                isGeneratingSummary = false,
                                summaryError = error.message ?: "Failed to generate summary"
                            )
                        }
                    }
                )
            }
        }
    }
}
