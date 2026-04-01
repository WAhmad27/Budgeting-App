package com.budgetapp.domain.usecase.savings

import com.budgetapp.domain.model.SavingsGoal
import com.budgetapp.domain.repository.ISavingsGoalRepository
import javax.inject.Inject

class AddSavingsGoalUseCase @Inject constructor(private val repo: ISavingsGoalRepository) {
    suspend operator fun invoke(goal: SavingsGoal): Result<Long> = runCatching {
        require(goal.name.isNotBlank()) { "Name cannot be empty" }
        require(goal.targetAmount > 0) { "Target amount must be greater than 0" }
        repo.addGoal(goal)
    }
}
