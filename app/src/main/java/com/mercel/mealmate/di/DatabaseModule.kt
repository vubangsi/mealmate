package com.mercel.mealmate.di

import android.content.Context
import androidx.room.Room
import com.mercel.mealmate.core.util.Constants
import com.mercel.mealmate.data.local.MealMateDatabase
import com.mercel.mealmate.data.local.dao.MealPlanDao
import com.mercel.mealmate.data.local.dao.RecipeDao
import com.mercel.mealmate.data.local.dao.ShoppingDao
import com.mercel.mealmate.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMealMateDatabase(
        @ApplicationContext context: Context
    ): MealMateDatabase {
        return Room.databaseBuilder(
            context,
            MealMateDatabase::class.java,
            Constants.DATABASE_NAME
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    fun provideRecipeDao(database: MealMateDatabase): RecipeDao {
        return database.recipeDao()
    }

    @Provides
    fun provideMealPlanDao(database: MealMateDatabase): MealPlanDao {
        return database.mealPlanDao()
    }

    @Provides
    fun provideShoppingDao(database: MealMateDatabase): ShoppingDao {
        return database.shoppingDao()
    }

    @Provides
    fun provideUserDao(database: MealMateDatabase): UserDao {
        return database.userDao()
    }
}
