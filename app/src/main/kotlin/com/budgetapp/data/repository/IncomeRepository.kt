package com.budgetapp.data.repository

import com.budgetapp.data.local.dao.IncomeDao
import com.budgetapp.data.mapper.toDomain
import com.budgetapp.data.mapper.toEntity
import com.budgetapp.domain.model.Income
import com.budgetapp.domain.repository.IIncomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class IncomeRepository @Inject constructor(
    private val dao: IncomeDao
) : IIncomeRepository {

    override fun getAllIncome(): Flow<List<Income>> =
        dao.getAllIncome().map { list -> list.map { it.toDomain() } }

    override fun getIncomeByDateRange(start: LocalDate, end: LocalDate): Flow<List<Income>> =
        dao.getIncomeByDateRange(start.toEpochDay(), end.toEpochDay())
            .map { list -> list.map { it.toDomain() } }

    override suspend fun getTotalIncome(start: LocalDate, end: LocalDate): Double =
        dao.getTotalIncome(start.toEpochDay(), end.toEpochDay()) ?: 0.0

    override suspend fun addIncome(income: Income): Long =
        dao.insertIncome(income.toEntity())

    override suspend fun updateIncome(income: Income) =
        dao.updateIncome(income.toEntity())

    override suspend fun deleteIncome(income: Income) =
        dao.deleteIncome(income.toEntity())

    override suspend fun getIncomeById(id: Long): Income? =
        dao.getIncomeById(id)?.toDomain()
}
