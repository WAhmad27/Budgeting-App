package com.budgetapp.data.repository

import com.budgetapp.data.local.dao.SavingsGoalDao
import com.budgetapp.data.mapper.toDomain
import com.budgetapp.data.mapper.toEntity
import com.budgetapp.domain.model.SavingsGoal
import com.budgetapp.domain.repository.ISavingsGoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SavingsGoalRepository @Inject constructor(
    private val dao: SavingsGoalDao
) : ISavingsGoalRepository {

    override fun getAllGoals(): Flow<List<SavingsGoal>> =
        dao.getAllGoals().map { list -> list.map { it.toDomain() } }

    override fun getActiveGoals(): Flow<List<SavingsGoal>> =
        dao.getActiveGoals().map { list -> list.map { it.toDomain() } }

    override suspend fun getGoalById(id: Long): SavingsGoal? =
        dao.getGoalById(id)?.toDomain()

    override suspend fun addGoal(goal: SavingsGoal): Long =
        dao.insertGoal(goal.toEntity())

    override suspend fun updateGoal(goal: SavingsGoal) =
        dao.updateGoal(goal.toEntity())

    override suspend fun deleteGoal(goal: SavingsGoal) =
        dao.deleteGoal(goal.toEntity())
}
