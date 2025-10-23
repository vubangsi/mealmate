package com.mercel.mealmate.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.mercel.mealmate.data.local.dao.MealPlanDao
import com.mercel.mealmate.data.local.dao.RecipeDao
import com.mercel.mealmate.data.local.dao.ShoppingDao
import com.mercel.mealmate.data.local.dao.UserDao
import com.mercel.mealmate.data.mapper.UserMapper
import com.mercel.mealmate.data.remote.api.SpoonacularApi
import com.mercel.mealmate.data.repository.*
import com.mercel.mealmate.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRecipeRepository(
        api: SpoonacularApi,
        dao: RecipeDao,
        @Named("spoonacular_api_key") apiKey: String
    ): RecipeRepository {
        return RecipeRepositoryImpl(api, dao, apiKey)
    }

    @Provides
    @Singleton
    fun provideMealPlanRepository(
        mealPlanDao: MealPlanDao,
        recipeDao: RecipeDao,
        aiRepository: AiRepository,
        recipeRepository: RecipeRepository
    ): MealPlanRepository {
        return MealPlanRepositoryImpl(mealPlanDao, recipeDao, aiRepository, recipeRepository)
    }

    @Provides
    @Singleton
    fun provideShoppingRepository(
        shoppingDao: ShoppingDao,
        mealPlanDao: MealPlanDao,
        recipeDao: RecipeDao,
        aiRepository: AiRepository
    ): ShoppingRepository {
        return ShoppingRepositoryImpl(shoppingDao, mealPlanDao, recipeDao, aiRepository)
    }

    @Provides
    @Singleton
    fun providePreferencesRepository(
        dataStore: DataStore<Preferences>
    ): PreferencesRepository {
        return PreferencesRepositoryImpl(dataStore)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        userDao: UserDao,
        dataStore: DataStore<Preferences>,
        userMapper: UserMapper
    ): AuthRepository {
        return AuthRepositoryImpl(userDao, dataStore, userMapper)
    }
}
