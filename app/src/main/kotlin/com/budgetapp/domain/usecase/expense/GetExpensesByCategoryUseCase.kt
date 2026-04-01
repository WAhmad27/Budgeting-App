package com.budgetapp.domain.usecase.expense

import com.budgetapp.domain.model.Category
import com.budgetapp.domain.repository.IExpenseRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetExpensesByCategoryUseCase @Inject constructor(private val repo: IExpenseRepository) {
    operator fun invoke(start: LocalDate, end: LocalDate): Flow<Map<Category, Double>> =
        repo.getSpendingByCategory(start, end)
}
