package com.budgetapp.domain.repository

import com.budgetapp.domain.model.Income
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface IIncomeRepository {
    fun getAllIncome(): Flow<List<Income>>
    fun getIncomeByDateRange(start: LocalDate, end: LocalDate): Flow<List<Income>>
    suspend fun getTotalIncome(start: LocalDate, end: LocalDate): Double
    suspend fun addIncome(income: Income): Long
    suspend fun updateIncome(income: Income)
    suspend fun deleteIncome(income: Income)
    suspend fun getIncomeById(id: Long): Income?
}
