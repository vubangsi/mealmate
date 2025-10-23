package com.mercel.mealmate.data.mapper

import com.mercel.mealmate.data.local.entity.ShoppingItemEntity
import com.mercel.mealmate.domain.model.ShoppingItem

fun ShoppingItemEntity.toDomain(): ShoppingItem {
    return ShoppingItem(
        id = id,
        ingredientName = ingredientName,
        quantity = quantity,
        section = section,
        estimatedPrice = estimatedPrice,
        notes = notes,
        checked = checked,
        isAvailableAtHome = isAvailableAtHome
    )
}

fun ShoppingItem.toEntity(): ShoppingItemEntity {
    return ShoppingItemEntity(
        id = id,
        ingredientName = ingredientName,
        quantity = quantity,
        section = section,
        estimatedPrice = estimatedPrice,
        notes = notes,
        checked = checked,
        isAvailableAtHome = isAvailableAtHome
    )
}
