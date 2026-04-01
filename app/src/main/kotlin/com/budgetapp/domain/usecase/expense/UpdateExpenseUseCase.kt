package com.budgetapp.domain.usecase.expense

import com.budgetapp.domain.model.Expense
import com.budgetapp.domain.repository.IExpenseRepository
import javax.inject.Inject

class UpdateExpenseUseCase @Inject constructor(private val repo: IExpenseRepository) {
    suspend operator fun invoke(expense: Expense): Result<Unit> = runCatching {
        require(expense.title.isNotBlank()) { "Title cannot be empty" }
        require(expense.amount > 0) { "Amount must be greater than 0" }
        repo.updateExpense(expense)
    }
}
