package com.budgetapp.domain.repository

import com.budgetapp.domain.model.SavingsGoal
import kotlinx.coroutines.flow.Flow

interface ISavingsGoalRepository {
    fun getAllGoals(): Flow<List<SavingsGoal>>
    fun getActiveGoals(): Flow<List<SavingsGoal>>
    suspend fun getGoalById(id: Long): SavingsGoal?
    suspend fun addGoal(goal: SavingsGoal): Long
    suspend fun updateGoal(goal: SavingsGoal)
    suspend fun deleteGoal(goal: SavingsGoal)
}
