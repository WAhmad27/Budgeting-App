package com.budgetapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "income")
data class IncomeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val amount: Double,
    val source: String,
    val isRecurring: Boolean,
    val recurrenceInterval: String?,
    val dateEpochDay: Long,
    val notes: String?
)
