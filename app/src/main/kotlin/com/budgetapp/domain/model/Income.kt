package com.budgetapp.domain.model

import java.time.LocalDate

data class Income(
    val id: Long = 0,
    val title: String,
    val amount: Double,
    val source: String,
    val isRecurring: Boolean,
    val recurrenceInterval: RecurrenceInterval?,
    val date: LocalDate,
    val notes: String?
)

enum class RecurrenceInterval(val label: String) {
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    YEARLY("Yearly")
}
