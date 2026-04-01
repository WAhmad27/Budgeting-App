package com.budgetapp.domain.model

import java.time.LocalDate

data class BudgetSummary(
    val totalIncome: Double,
    val totalExpenses: Double,
    val netBalance: Double,
    val savingsRate: Float,
    val topSpendingCategory: Category?,
    val period: DateRange
)

data class DateRange(val start: LocalDate, val end: LocalDate)
