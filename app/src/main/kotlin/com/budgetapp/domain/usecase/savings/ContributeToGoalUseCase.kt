package com.budgetapp.domain.usecase.savings

import com.budgetapp.domain.model.SavingsGoal
import com.budgetapp.domain.repository.ISavingsGoalRepository
import javax.inject.Inject

class ContributeToGoalUseCase @Inject constructor(private val repo: ISavingsGoalRepository) {
    suspend operator fun invoke(goal: SavingsGoal, amount: Double): Result<Unit> = runCatching {
        require(amount > 0) { "Contribution must be greater than 0" }
        val newCurrent = (goal.currentAmount + amount).coerceAtMost(goal.targetAmount)
        val updated = goal.copy(
            currentAmount = newCurrent,
            isCompleted = newCurrent >= goal.targetAmount
        )
        repo.updateGoal(updated)
    }
}
