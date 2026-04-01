package com.budgetapp.data.mapper

import com.budgetapp.data.local.entity.CategoryEntity
import com.budgetapp.domain.model.Category

fun CategoryEntity.toDomain() = Category(
    id = id,
    name = name,
    iconName = iconName,
    colorHex = colorHex,
    isDefault = isDefault,
    monthlyBudgetLimit = monthlyBudgetLimit
)

fun Category.toEntity() = CategoryEntity(
    id = id,
    name = name,
    iconName = iconName,
    colorHex = colorHex,
    isDefault = isDefault,
    monthlyBudgetLimit = monthlyBudgetLimit
)
