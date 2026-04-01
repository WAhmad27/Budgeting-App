package com.budgetapp.data.local.dao

import androidx.room.*
import com.budgetapp.data.local.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

data class CategorySpending(val categoryId: Long, val total: Double)

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expense ORDER BY dateEpochDay DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expense WHERE categoryId = :categoryId ORDER BY dateEpochDay DESC")
    fun getExpensesByCategory(categoryId: Long): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expense WHERE dateEpochDay >= :startDay AND dateEpochDay <= :endDay ORDER BY dateEpochDay DESC")
    fun getExpensesByDateRange(startDay: Long, endDay: Long): Flow<List<ExpenseEntity>>

    @Query("""
        SELECT categoryId, SUM(amount) as total
        FROM expense
        WHERE dateEpochDay >= :startDay AND dateEpochDay <= :endDay
        GROUP BY categoryId
        ORDER BY total DESC
    """)
    fun getSpendingByCategory(startDay: Long, endDay: Long): Flow<List<CategorySpending>>

    @Query("SELECT SUM(amount) FROM expense WHERE dateEpochDay >= :startDay AND dateEpochDay <= :endDay")
    suspend fun getTotalExpenses(startDay: Long, endDay: Long): Double?

    @Query("SELECT * FROM expense WHERE id = :id")
    suspend fun getExpenseById(id: Long): ExpenseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity): Long

    @Update
    suspend fun updateExpense(expense: ExpenseEntity)

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)
}
