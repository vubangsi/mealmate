package com.mercel.mealmate.domain.repository

import com.mercel.mealmate.domain.model.ShoppingItem
import kotlinx.coroutines.flow.Flow

interface ShoppingRepository {
    fun getShoppingList(): Flow<List<ShoppingItem>>
    
    suspend fun generateShoppingListFromPlan()
    
    suspend fun toggleItemChecked(itemId: String)
    
    suspend fun addItem(name: String, quantity: String, section: String?, estimatedPrice: Double? = null)
    
    suspend fun removeItem(itemId: String)
    
    suspend fun clearCheckedItems()
    
    suspend fun scanAvailableIngredients()
    
    suspend fun optimizeShoppingListWithAI()
    
    suspend fun placeDeliveryOrder()
    
    suspend fun updateItemPrice(itemId: String, price: Double)
    
    suspend fun markItemAsAvailableAtHome(itemId: String, isAvailable: Boolean)
}
