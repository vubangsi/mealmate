package com.mercel.mealmate.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mercel.mealmate.data.local.converter.Converters

@Entity(tableName = "recipes")
@TypeConverters(Converters::class)
data class RecipeEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val imageUrl: String?,
    val servings: Int,
    val readyInMinutes: Int,
    val cuisines: List<String>,
    val diets: List<String>,
    val ingredients: List<IngredientEntity>,
    val steps: List<String>,
    val nutrients: NutrientsEntity,
    val isFavorite: Boolean = false,
    val cachedAt: Long = System.currentTimeMillis()
)

data class IngredientEntity(
    val name: String,
    val quantity: String,
    val section: String? = null
)

data class NutrientsEntity(
    val calories: Int,
    val protein: Double,
    val carbs: Double,
    val fat: Double
)
