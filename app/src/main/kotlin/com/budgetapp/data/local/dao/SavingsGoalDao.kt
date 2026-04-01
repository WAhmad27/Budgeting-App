package com.budgetapp.data.local.dao

import androidx.room.*
import com.budgetapp.data.local.entity.SavingsGoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavingsGoalDao {

    @Query("SELECT * FROM savings_goal ORDER BY isCompleted ASC, targetDateEpochDay ASC")
    fun getAllGoals(): Flow<List<SavingsGoalEntity>>

    @Query("SELECT * FROM savings_goal WHERE isCompleted = 0 ORDER BY targetDateEpochDay ASC")
    fun getActiveGoals(): Flow<List<SavingsGoalEntity>>

    @Query("SELECT * FROM savings_goal WHERE id = :id")
    suspend fun getGoalById(id: Long): SavingsGoalEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: SavingsGoalEntity): Long

    @Update
    suspend fun updateGoal(goal: SavingsGoalEntity)

    @Delete
    suspend fun deleteGoal(goal: SavingsGoalEntity)
}
