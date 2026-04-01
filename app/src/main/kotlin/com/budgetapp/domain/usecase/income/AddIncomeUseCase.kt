package com.budgetapp.domain.usecase.income

import com.budgetapp.domain.model.Income
import com.budgetapp.domain.repository.IIncomeRepository
import javax.inject.Inject

class AddIncomeUseCase @Inject constructor(private val repo: IIncomeRepository) {
    suspend operator fun invoke(income: Income): Result<Long> = runCatching {
        require(income.title.isNotBlank()) { "Title cannot be empty" }
        require(income.amount > 0) { "Amount must be greater than 0" }
        repo.addIncome(income)
    }
}
