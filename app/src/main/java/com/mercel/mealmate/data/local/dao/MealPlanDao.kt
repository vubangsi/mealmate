package com.mercel.mealmate.data.local.dao

import androidx.room.*
import com.mercel.mealmate.data.local.entity.MealEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealPlanDao {
    @Query("SELECT * FROM meal_entries ORDER BY dayOfWeek, slot")
    fun getAllMealEntries(): Flow<List<MealEntryEntity>>

    @Query("SELECT * FROM meal_entries WHERE dayOfWeek = :dayOfWeek ORDER BY slot")
    fun getMealEntriesByDay(dayOfWeek: Int): Flow<List<MealEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealEntry(entry: MealEntryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealEntries(entries: List<MealEntryEntity>)

    @Query("DELETE FROM meal_entries WHERE id = :id")
    suspend fun deleteMealEntry(id: String)

    @Query("DELETE FROM meal_entries")
    suspend fun deleteAll()
}
