package com.budgetapp.domain.usecase.savings

import com.budgetapp.domain.model.SavingsGoal
import com.budgetapp.domain.repository.ISavingsGoalRepository
import javax.inject.Inject

class DeleteSavingsGoalUseCase @Inject constructor(private val repo: ISavingsGoalRepository) {
    suspend operator fun invoke(goal: SavingsGoal) = repo.deleteGoal(goal)
}
