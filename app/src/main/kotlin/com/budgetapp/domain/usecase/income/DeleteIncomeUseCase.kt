package com.budgetapp.domain.usecase.income

import com.budgetapp.domain.model.Income
import com.budgetapp.domain.repository.IIncomeRepository
import javax.inject.Inject

class DeleteIncomeUseCase @Inject constructor(private val repo: IIncomeRepository) {
    suspend operator fun invoke(income: Income) = repo.deleteIncome(income)
}
