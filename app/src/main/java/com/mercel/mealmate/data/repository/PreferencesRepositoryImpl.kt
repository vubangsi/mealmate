package com.mercel.mealmate.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.mercel.mealmate.domain.model.AppTheme
import com.mercel.mealmate.domain.model.UserPrefs
import com.mercel.mealmate.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PreferencesRepository {

    private object PreferencesKeys {
        val DIET = stringPreferencesKey("diet")
        val INTOLERANCES = stringSetPreferencesKey("intolerances")
        val CALORIES_PER_DAY = intPreferencesKey("calories_per_day")
        val BUDGET_PER_WEEK = intPreferencesKey("budget_per_week")
        val THEME = stringPreferencesKey("theme")
    }

    override val userPrefs: Flow<UserPrefs> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            UserPrefs(
                diet = preferences[PreferencesKeys.DIET],
                intolerances = preferences[PreferencesKeys.INTOLERANCES] ?: emptySet(),
                caloriesPerDay = preferences[PreferencesKeys.CALORIES_PER_DAY],
                budgetPerWeek = preferences[PreferencesKeys.BUDGET_PER_WEEK],
                theme = AppTheme.valueOf(
                    preferences[PreferencesKeys.THEME] ?: AppTheme.SYSTEM.name
                )
            )
        }

    override suspend fun updateDiet(diet: String?) {
        dataStore.edit { preferences ->
            if (diet != null) {
                preferences[PreferencesKeys.DIET] = diet
            } else {
                preferences.remove(PreferencesKeys.DIET)
            }
        }
    }

    override suspend fun updateIntolerances(intolerances: Set<String>) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.INTOLERANCES] = intolerances
        }
    }

    override suspend fun updateCaloriesPerDay(calories: Int?) {
        dataStore.edit { preferences ->
            if (calories != null) {
                preferences[PreferencesKeys.CALORIES_PER_DAY] = calories
            } else {
                preferences.remove(PreferencesKeys.CALORIES_PER_DAY)
            }
        }
    }

    override suspend fun updateBudgetPerWeek(budget: Int?) {
        dataStore.edit { preferences ->
            if (budget != null) {
                preferences[PreferencesKeys.BUDGET_PER_WEEK] = budget
            } else {
                preferences.remove(PreferencesKeys.BUDGET_PER_WEEK)
            }
        }
    }

    override suspend fun updateTheme(theme: AppTheme) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME] = theme.name
        }
    }
}
