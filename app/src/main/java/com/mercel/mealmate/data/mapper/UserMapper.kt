package com.mercel.mealmate.data.mapper

import com.mercel.mealmate.data.local.entity.UserEntity
import com.mercel.mealmate.domain.model.User
import com.mercel.mealmate.domain.model.UserPrefs
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserMapper @Inject constructor() {
    
    fun toDomain(entity: UserEntity): User {
        return User(
            id = entity.id,
            email = entity.email,
            name = entity.name,
            createdAt = entity.createdAt,
            preferences = UserPrefs(
                diet = entity.diet,
                intolerances = entity.intolerances,
                caloriesPerDay = entity.caloriesPerDay,
                budgetPerWeek = entity.budgetPerWeek,
                theme = entity.theme
            )
        )
    }
    
    fun toEntity(domain: User): UserEntity {
        return UserEntity(
            id = domain.id,
            email = domain.email,
            name = domain.name,
            createdAt = domain.createdAt,
            diet = domain.preferences.diet,
            intolerances = domain.preferences.intolerances,
            caloriesPerDay = domain.preferences.caloriesPerDay,
            budgetPerWeek = domain.preferences.budgetPerWeek,
            theme = domain.preferences.theme
        )
    }
}