package com.mercel.mealmate.data.local.converter

import androidx.room.TypeConverter
import com.mercel.mealmate.data.local.entity.IngredientEntity
import com.mercel.mealmate.data.local.entity.NutrientsEntity
import com.mercel.mealmate.domain.model.AppTheme
import com.mercel.mealmate.domain.model.MealSlot
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        val adapter = moshi.adapter<List<String>>(
            Types.newParameterizedType(List::class.java, String::class.java)
        )
        return adapter.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val adapter = moshi.adapter<List<String>>(
            Types.newParameterizedType(List::class.java, String::class.java)
        )
        return adapter.fromJson(value) ?: emptyList()
    }

    @TypeConverter
    fun fromIngredientList(value: List<IngredientEntity>): String {
        val adapter = moshi.adapter<List<IngredientEntity>>(
            Types.newParameterizedType(List::class.java, IngredientEntity::class.java)
        )
        return adapter.toJson(value)
    }

    @TypeConverter
    fun toIngredientList(value: String): List<IngredientEntity> {
        val adapter = moshi.adapter<List<IngredientEntity>>(
            Types.newParameterizedType(List::class.java, IngredientEntity::class.java)
        )
        return adapter.fromJson(value) ?: emptyList()
    }

    @TypeConverter
    fun fromNutrients(value: NutrientsEntity): String {
        val adapter = moshi.adapter(NutrientsEntity::class.java)
        return adapter.toJson(value)
    }

    @TypeConverter
    fun toNutrients(value: String): NutrientsEntity {
        val adapter = moshi.adapter(NutrientsEntity::class.java)
        return adapter.fromJson(value) ?: NutrientsEntity(0, 0.0, 0.0, 0.0)
    }

    @TypeConverter
    fun fromStringSet(value: Set<String>): String {
        val adapter = moshi.adapter<Set<String>>(
            Types.newParameterizedType(Set::class.java, String::class.java)
        )
        return adapter.toJson(value)
    }

    @TypeConverter
    fun toStringSet(value: String): Set<String> {
        val adapter = moshi.adapter<Set<String>>(
            Types.newParameterizedType(Set::class.java, String::class.java)
        )
        return adapter.fromJson(value) ?: emptySet()
    }

    @TypeConverter
    fun fromAppTheme(value: AppTheme): String = value.name

    @TypeConverter
    fun toAppTheme(value: String): AppTheme = AppTheme.valueOf(value)

    @TypeConverter
    fun fromMealSlot(value: MealSlot): String = value.name

    @TypeConverter
    fun toMealSlot(value: String): MealSlot = MealSlot.valueOf(value)
}
