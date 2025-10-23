package com.mercel.mealmate.domain.usecase

import com.mercel.mealmate.domain.repository.MealPlanRepository
import javax.inject.Inject

class GenerateWeeklyPlanUseCase @Inject constructor(
    private val repository: MealPlanRepository
) {
    suspend operator fun invoke(preferences: Map<String, Any>) {
        repository.generateWeeklyPlan(preferences)
    }
}
