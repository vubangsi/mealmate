package com.mercel.mealmate.presentation.shopping.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercel.mealmate.domain.repository.ShoppingRepository
import com.mercel.mealmate.presentation.shopping.uistate.ShoppingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val shoppingRepository: ShoppingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShoppingUiState())
    val uiState: StateFlow<ShoppingUiState> = _uiState.asStateFlow()

    init {
        loadShoppingList()
    }

    private fun loadShoppingList() {
        viewModelScope.launch {
            shoppingRepository.getShoppingList().collect { items ->
                _uiState.update { it.copy(items = items, isLoading = false) }
            }
        }
    }

    fun generateFromPlan() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                shoppingRepository.generateShoppingListFromPlan()
                _uiState.update { it.copy(isLoading = false, error = null) }
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Failed to generate shopping list. Please try again."
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = errorMessage
                    )
                }
            }
        }
    }

    fun toggleItemChecked(itemId: String) {
        viewModelScope.launch {
            shoppingRepository.toggleItemChecked(itemId)
        }
    }

    fun removeItem(itemId: String) {
        viewModelScope.launch {
            shoppingRepository.removeItem(itemId)
        }
    }

    fun clearCheckedItems() {
        viewModelScope.launch {
            shoppingRepository.clearCheckedItems()
        }
    }

    fun scanAvailableIngredients() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                shoppingRepository.scanAvailableIngredients()
                _uiState.update { it.copy(isLoading = false, error = null) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to scan ingredients: ${e.message}"
                    )
                }
            }
        }
    }

    fun optimizeShoppingList() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                shoppingRepository.optimizeShoppingListWithAI()
                _uiState.update { it.copy(isLoading = false, error = null) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to optimize list: ${e.message}"
                    )
                }
            }
        }
    }

    fun editItem(itemId: String) {
        // TODO: Implement item editing dialog
    }

    fun placeOrder() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                shoppingRepository.placeDeliveryOrder()
                _uiState.update { it.copy(isLoading = false, error = null) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to place order: ${e.message}"
                    )
                }
            }
        }
    }
}
