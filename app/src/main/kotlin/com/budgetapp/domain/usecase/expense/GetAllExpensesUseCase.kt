package com.budgetapp.domain.usecase.expense

import com.budgetapp.domain.model.Expense
import com.budgetapp.domain.repository.IExpenseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllExpensesUseCase @Inject constructor(private val repo: IExpenseRepository) {
    operator fun invoke(): Flow<List<Expense>> = repo.getAllExpenses()
}
