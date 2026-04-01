package com.budgetapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.budgetapp.data.local.dao.*
import com.budgetapp.data.local.entity.*

@Database(
    entities = [
        IncomeEntity::class,
        ExpenseEntity::class,
        CategoryEntity::class,
        SavingsGoalEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class BudgetDatabase : RoomDatabase() {
    abstract fun incomeDao(): IncomeDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun categoryDao(): CategoryDao
    abstract fun savingsGoalDao(): SavingsGoalDao

    companion object {
        const val DATABASE_NAME = "budget_db"
    }
}
