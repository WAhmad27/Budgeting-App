package com.budgetapp.domain.usecase.income

import com.budgetapp.domain.model.Income
import com.budgetapp.domain.repository.IIncomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllIncomeUseCase @Inject constructor(private val repo: IIncomeRepository) {
    operator fun invoke(): Flow<List<Income>> = repo.getAllIncome()
}
