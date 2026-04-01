package com.budgetapp.data.mapper

import com.budgetapp.data.local.entity.IncomeEntity
import com.budgetapp.domain.model.Income
import com.budgetapp.domain.model.RecurrenceInterval
import java.time.LocalDate

fun IncomeEntity.toDomain() = Income(
    id = id,
    title = title,
    amount = amount,
    source = source,
    isRecurring = isRecurring,
    recurrenceInterval = recurrenceInterval?.let { RecurrenceInterval.valueOf(it) },
    date = LocalDate.ofEpochDay(dateEpochDay),
    notes = notes
)

fun Income.toEntity() = IncomeEntity(
    id = id,
    title = title,
    amount = amount,
    source = source,
    isRecurring = isRecurring,
    recurrenceInterval = recurrenceInterval?.name,
    dateEpochDay = date.toEpochDay(),
    notes = notes
)
