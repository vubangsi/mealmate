package com.mercel.mealmate.data.remote.api

import com.mercel.mealmate.data.remote.dto.OpenAiRequest
import com.mercel.mealmate.data.remote.dto.OpenAiResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenAiApi {
    @POST("v1/chat/completions")
    suspend fun generateCompletion(
        @Header("Authorization") authHeader: String,
        @Body request: OpenAiRequest
    ): OpenAiResponse
}