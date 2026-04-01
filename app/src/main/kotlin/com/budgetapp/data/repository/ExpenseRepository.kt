package com.budgetapp.data.repository

import com.budgetapp.data.local.dao.ExpenseDao
import com.budgetapp.data.mapper.toDomain
import com.budgetapp.data.mapper.toEntity
import com.budgetapp.domain.model.Category
import com.budgetapp.domain.model.Expense
import com.budgetapp.domain.repository.ICategoryRepository
import com.budgetapp.domain.repository.IExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class ExpenseRepository @Inject constructor(
    private val expenseDao: ExpenseDao,
    private val categoryRepo: ICategoryRepository
) : IExpenseRepository {

    override fun getAllExpenses(): Flow<List<Expense>> =
        combine(
            expenseDao.getAllExpenses(),
            categoryRepo.getAllCategories()
        ) { expenses, categories ->
            val categoryMap = categories.associateBy { it.id }
            expenses.mapNotNull { entity ->
                categoryMap[entity.categoryId]?.let { entity.toDomain(it) }
            }
        }

    override fun getExpensesByCategory(categoryId: Long): Flow<List<Expense>> =
        combine(
            expenseDao.getExpensesByCategory(categoryId),
            categoryRepo.getAllCategories()
        ) { expenses, categories ->
            val categoryMap = categories.associateBy { it.id }
            expenses.mapNotNull { entity ->
                categoryMap[entity.categoryId]?.let { entity.toDomain(it) }
            }
        }

    override fun getExpensesByDateRange(start: LocalDate, end: LocalDate): Flow<List<Expense>> =
        combine(
            expenseDao.getExpensesByDateRange(start.toEpochDay(), end.toEpochDay()),
            categoryRepo.getAllCategories()
        ) { expenses, categories ->
            val categoryMap = categories.associateBy { it.id }
            expenses.mapNotNull { entity ->
                categoryMap[entity.categoryId]?.let { entity.toDomain(it) }
            }
        }

    override fun getSpendingByCategory(start: LocalDate, end: LocalDate): Flow<Map<Category, Double>> =
        combine(
            expenseDao.getSpendingByCategory(start.toEpochDay(), end.toEpochDay()),
            categoryRepo.getAllCategories()
        ) { spending, categories ->
            val categoryMap = categories.associateBy { it.id }
            spending.mapNotNull { cs ->
                categoryMap[cs.categoryId]?.let { it to cs.total }
            }.toMap()
        }

    override suspend fun getTotalExpenses(start: LocalDate, end: LocalDate): Double =
        expenseDao.getTotalExpenses(start.toEpochDay(), end.toEpochDay()) ?: 0.0

    override suspend fun addExpense(expense: Expense): Long =
        expenseDao.insertExpense(expense.toEntity())

    override suspend fun updateExpense(expense: Expense) =
        expenseDao.updateExpense(expense.toEntity())

    override suspend fun deleteExpense(expense: Expense) =
        expenseDao.deleteExpense(expense.toEntity())

    override suspend fun getExpenseById(id: Long): Expense? {
        val entity = expenseDao.getExpenseById(id) ?: return null
        val category = categoryRepo.getCategoryById(entity.categoryId) ?: return null
        return entity.toDomain(category)
    }
}
