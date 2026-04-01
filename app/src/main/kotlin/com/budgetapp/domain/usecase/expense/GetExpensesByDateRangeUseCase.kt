package com.budgetapp.domain.usecase.expense

import com.budgetapp.domain.model.Expense
import com.budgetapp.domain.repository.IExpenseRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetExpensesByDateRangeUseCase @Inject constructor(private val repo: IExpenseRepository) {
    operator fun invoke(start: LocalDate, end: LocalDate): Flow<List<Expense>> =
        repo.getExpensesByDateRange(start, end)
}
