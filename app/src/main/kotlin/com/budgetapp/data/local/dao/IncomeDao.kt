package com.budgetapp.data.local.dao

import androidx.room.*
import com.budgetapp.data.local.entity.IncomeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeDao {

    @Query("SELECT * FROM income ORDER BY dateEpochDay DESC")
    fun getAllIncome(): Flow<List<IncomeEntity>>

    @Query("SELECT * FROM income WHERE dateEpochDay >= :startDay AND dateEpochDay <= :endDay ORDER BY dateEpochDay DESC")
    fun getIncomeByDateRange(startDay: Long, endDay: Long): Flow<List<IncomeEntity>>

    @Query("SELECT SUM(amount) FROM income WHERE dateEpochDay >= :startDay AND dateEpochDay <= :endDay")
    suspend fun getTotalIncome(startDay: Long, endDay: Long): Double?

    @Query("SELECT * FROM income WHERE id = :id")
    suspend fun getIncomeById(id: Long): IncomeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncome(income: IncomeEntity): Long

    @Update
    suspend fun updateIncome(income: IncomeEntity)

    @Delete
    suspend fun deleteIncome(income: IncomeEntity)
}
