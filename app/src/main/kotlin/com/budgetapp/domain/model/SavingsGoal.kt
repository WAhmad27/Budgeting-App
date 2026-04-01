package com.budgetapp.domain.model

import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class SavingsGoal(
    val id: Long = 0,
    val name: String,
    val targetAmount: Double,
    val currentAmount: Double,
    val targetDate: LocalDate?,
    val iconName: String,
    val colorHex: String,
    val isCompleted: Boolean = false,
    val createdDate: LocalDate
) {
    val progressPercent: Float
        get() = if (targetAmount > 0) (currentAmount / targetAmount).toFloat().coerceIn(0f, 1f) else 0f

    val remainingAmount: Double
        get() = (targetAmount - currentAmount).coerceAtLeast(0.0)

    val daysRemaining: Long?
        get() = targetDate?.let { ChronoUnit.DAYS.between(LocalDate.now(), it) }
}
