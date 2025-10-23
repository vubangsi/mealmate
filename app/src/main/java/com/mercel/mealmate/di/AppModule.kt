package com.mercel.mealmate.di

import android.content.Context
import com.mercel.mealmate.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @Named("spoonacular_api_key")
    fun provideSpoonacularApiKey(): String {
        // In production, this should be loaded from BuildConfig or secure storage
        return BuildConfig.SPOONACULAR_API_KEY.ifEmpty { "demo_key" }
    }

    @Provides
    @Singleton
    @Named("openai_api_key")
    fun provideOpenAiApiKey(): String {
        // In production, this should be loaded from BuildConfig or secure storage
        return BuildConfig.OPENAI_API_KEY.ifEmpty { "demo_key" }
    }

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }
}
