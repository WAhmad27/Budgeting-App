package com.budgetapp.domain.usecase.expense

import com.budgetapp.domain.model.Expense
import com.budgetapp.domain.repository.IExpenseRepository
import javax.inject.Inject

class DeleteExpenseUseCase @Inject constructor(private val repo: IExpenseRepository) {
    suspend operator fun invoke(expense: Expense) = repo.deleteExpense(expense)
}
