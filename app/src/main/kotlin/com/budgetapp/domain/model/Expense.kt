package com.budgetapp.domain.model

import java.time.LocalDate

data class Expense(
    val id: Long = 0,
    val title: String,
    val amount: Double,
    val category: Category,
    val date: LocalDate,
    val notes: String?,
    val isRecurring: Boolean,
    val recurrenceInterval: RecurrenceInterval?
)
