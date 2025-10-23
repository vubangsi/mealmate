package com.mercel.mealmate.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mercel.mealmate.data.local.converter.Converters
import com.mercel.mealmate.domain.model.AppTheme

@Entity(tableName = "users")
@TypeConverters(Converters::class)
data class UserEntity(
    @PrimaryKey
    val id: String,
    val email: String,
    val name: String,
    val createdAt: Long,
    val diet: String?,
    val intolerances: Set<String>,
    val caloriesPerDay: Int?,
    val budgetPerWeek: Int?,
    val theme: AppTheme
)