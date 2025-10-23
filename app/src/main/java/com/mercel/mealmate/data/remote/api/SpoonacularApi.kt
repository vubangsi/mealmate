package com.mercel.mealmate.data.remote.api

import com.mercel.mealmate.data.remote.dto.RecipeDto
import com.mercel.mealmate.data.remote.dto.RecipeSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpoonacularApi {
    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("query") query: String,
        @Query("diet") diet: String? = null,
        @Query("intolerances") intolerances: String? = null,
        @Query("addRecipeInformation") addRecipeInformation: Boolean = true,
        @Query("addRecipeNutrition") addRecipeNutrition: Boolean = true,
        @Query("number") number: Int = 20,
        @Query("apiKey") apiKey: String
    ): RecipeSearchResponse

    @GET("recipes/{id}/information")
    suspend fun getRecipeById(
        @Path("id") id: Int,
        @Query("includeNutrition") includeNutrition: Boolean = true,
        @Query("apiKey") apiKey: String
    ): RecipeDto
}
