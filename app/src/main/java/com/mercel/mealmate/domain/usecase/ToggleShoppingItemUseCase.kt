package com.mercel.mealmate.domain.usecase

import com.mercel.mealmate.domain.repository.ShoppingRepository
import javax.inject.Inject

class ToggleShoppingItemUseCase @Inject constructor(
    private val repository: ShoppingRepository
) {
    suspend operator fun invoke(itemId: String) {
        repository.toggleItemChecked(itemId)
    }
}
