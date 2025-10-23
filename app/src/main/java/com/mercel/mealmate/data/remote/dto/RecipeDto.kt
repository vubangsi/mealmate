package com.mercel.mealmate.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecipeSearchResponse(
    @Json(name = "results")
    val results: List<RecipeDto>,
    @Json(name = "offset")
    val offset: Int,
    @Json(name = "number")
    val number: Int,
    @Json(name = "totalResults")
    val totalResults: Int
)

@JsonClass(generateAdapter = true)
data class RecipeDto(
    @Json(name = "id")
    val id: Int,
    @Json(name = "title")
    val title: String,
    @Json(name = "image")
    val image: String?,
    @Json(name = "servings")
    val servings: Int?,
    @Json(name = "readyInMinutes")
    val readyInMinutes: Int?,
    @Json(name = "cuisines")
    val cuisines: List<String>?,
    @Json(name = "diets")
    val diets: List<String>?,
    @Json(name = "extendedIngredients")
    val extendedIngredients: List<IngredientDto>?,
    @Json(name = "analyzedInstructions")
    val analyzedInstructions: List<InstructionDto>?,
    @Json(name = "nutrition")
    val nutrition: NutritionDto?
)

@JsonClass(generateAdapter = true)
data class IngredientDto(
    @Json(name = "name")
    val name: String,
    @Json(name = "original")
    val original: String,
    @Json(name = "aisle")
    val aisle: String?
)

@JsonClass(generateAdapter = true)
data class InstructionDto(
    @Json(name = "steps")
    val steps: List<StepDto>
)

@JsonClass(generateAdapter = true)
data class StepDto(
    @Json(name = "number")
    val number: Int,
    @Json(name = "step")
    val step: String
)

@JsonClass(generateAdapter = true)
data class NutritionDto(
    @Json(name = "nutrients")
    val nutrients: List<NutrientDto>
)

@JsonClass(generateAdapter = true)
data class NutrientDto(
    @Json(name = "name")
    val name: String,
    @Json(name = "amount")
    val amount: Double,
    @Json(name = "unit")
    val unit: String
)
