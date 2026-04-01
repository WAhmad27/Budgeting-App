package com.budgetapp.data.mapper

import com.budgetapp.data.local.entity.SavingsGoalEntity
import com.budgetapp.domain.model.SavingsGoal
import java.time.LocalDate

fun SavingsGoalEntity.toDomain() = SavingsGoal(
    id = id,
    name = name,
    targetAmount = targetAmount,
    currentAmount = currentAmount,
    targetDate = targetDateEpochDay?.let { LocalDate.ofEpochDay(it) },
    iconName = iconName,
    colorHex = colorHex,
    isCompleted = isCompleted,
    createdDate = LocalDate.ofEpochDay(createdDateEpochDay)
)

fun SavingsGoal.toEntity() = SavingsGoalEntity(
    id = id,
    name = name,
    targetAmount = targetAmount,
    currentAmount = currentAmount,
    targetDateEpochDay = targetDate?.toEpochDay(),
    iconName = iconName,
    colorHex = colorHex,
    isCompleted = isCompleted,
    createdDateEpochDay = createdDate.toEpochDay()
)
