package com.budgetapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "savings_goal")
data class SavingsGoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val targetAmount: Double,
    val currentAmount: Double,
    val targetDateEpochDay: Long?,
    val iconName: String,
    val colorHex: String,
    val isCompleted: Boolean = false,
    val createdDateEpochDay: Long
)
