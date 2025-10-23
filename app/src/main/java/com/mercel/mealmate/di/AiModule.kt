package com.mercel.mealmate.di

import com.mercel.mealmate.data.remote.api.OpenAiApi
import com.mercel.mealmate.data.repository.AiRepositoryImpl
import com.mercel.mealmate.domain.repository.AiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AiModule {

    @Provides
    @Singleton
    fun provideAiRepository(
        openAiApi: OpenAiApi,
        @Named("openai_api_key") apiKey: String
    ): AiRepository {
        return AiRepositoryImpl(openAiApi, apiKey)
    }
}
