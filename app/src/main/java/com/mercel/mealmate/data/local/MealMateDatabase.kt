package com.mercel.mealmate.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mercel.mealmate.data.local.converter.Converters
import com.mercel.mealmate.data.local.dao.MealPlanDao
import com.mercel.mealmate.data.local.dao.RecipeDao
import com.mercel.mealmate.data.local.dao.ShoppingDao
import com.mercel.mealmate.data.local.dao.UserDao
import com.mercel.mealmate.data.local.entity.MealEntryEntity
import com.mercel.mealmate.data.local.entity.RecipeEntity
import com.mercel.mealmate.data.local.entity.ShoppingItemEntity
import com.mercel.mealmate.data.local.entity.UserEntity

@Database(
    entities = [
        RecipeEntity::class,
        MealEntryEntity::class,
        ShoppingItemEntity::class,
        UserEntity::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MealMateDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun mealPlanDao(): MealPlanDao
    abstract fun shoppingDao(): ShoppingDao
    abstract fun userDao(): UserDao
    
    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `users` (
                        `id` TEXT NOT NULL,
                        `email` TEXT NOT NULL,
                        `name` TEXT NOT NULL,
                        `createdAt` INTEGER NOT NULL,
                        `diet` TEXT,
                        `intolerances` TEXT NOT NULL,
                        `caloriesPerDay` INTEGER,
                        `budgetPerWeek` INTEGER,
                        `theme` TEXT NOT NULL,
                        PRIMARY KEY(`id`)
                    )
                """.trimIndent())
            }
        }
    }
}
