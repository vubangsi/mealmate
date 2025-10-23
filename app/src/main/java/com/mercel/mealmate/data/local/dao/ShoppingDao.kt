package com.mercel.mealmate.data.local.dao

import androidx.room.*
import com.mercel.mealmate.data.local.entity.ShoppingItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingDao {
    @Query("SELECT * FROM shopping_items ORDER BY checked, section, ingredientName")
    fun getAllItems(): Flow<List<ShoppingItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ShoppingItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<ShoppingItemEntity>)

    @Update
    suspend fun updateItem(item: ShoppingItemEntity)

    @Query("UPDATE shopping_items SET checked = :checked WHERE id = :id")
    suspend fun updateCheckedStatus(id: String, checked: Boolean)

    @Query("DELETE FROM shopping_items WHERE id = :id")
    suspend fun deleteItem(id: String)

    @Query("DELETE FROM shopping_items WHERE checked = 1")
    suspend fun deleteCheckedItems()

    @Query("DELETE FROM shopping_items")
    suspend fun deleteAll()
}
