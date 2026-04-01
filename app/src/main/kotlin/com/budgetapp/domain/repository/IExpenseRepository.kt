package com.budgetapp.domain.repository

import com.budgetapp.domain.model.Category
import com.budgetapp.domain.model.Expense
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface IExpenseRepository {
    fun getAllExpenses(): Flow<List<Expense>>
    fun getExpensesByCategory(categoryId: Long): Flow<List<Expense>>
    fun getExpensesByDateRange(start: LocalDate, end: LocalDate): Flow<List<Expense>>
    fun getSpendingByCategory(start: LocalDate, end: LocalDate): Flow<Map<Category, Double>>
    suspend fun getTotalExpenses(start: LocalDate, end: LocalDate): Double
    suspend fun addExpense(expense: Expense): Long
    suspend fun updateExpense(expense: Expense)
    suspend fun deleteExpense(expense: Expense)
    suspend fun getExpenseById(id: Long): Expense?
}
