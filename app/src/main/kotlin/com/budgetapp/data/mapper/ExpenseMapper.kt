package com.budgetapp.data.mapper

import com.budgetapp.data.local.entity.ExpenseEntity
import com.budgetapp.domain.model.Category
import com.budgetapp.domain.model.Expense
import com.budgetapp.domain.model.RecurrenceInterval
import java.time.LocalDate

fun ExpenseEntity.toDomain(category: Category) = Expense(
    id = id,
    title = title,
    amount = amount,
    category = category,
    date = LocalDate.ofEpochDay(dateEpochDay),
    notes = notes,
    isRecurring = isRecurring,
    recurrenceInterval = recurrenceInterval?.let { RecurrenceInterval.valueOf(it) }
)

fun Expense.toEntity() = ExpenseEntity(
    id = id,
    title = title,
    amount = amount,
    categoryId = category.id,
    dateEpochDay = date.toEpochDay(),
    notes = notes,
    isRecurring = isRecurring,
    recurrenceInterval = recurrenceInterval?.name
)
