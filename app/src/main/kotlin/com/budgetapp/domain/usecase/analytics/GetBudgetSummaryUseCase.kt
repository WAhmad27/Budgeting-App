package com.budgetapp.domain.usecase.analytics

import com.budgetapp.domain.model.BudgetSummary
import com.budgetapp.domain.model.DateRange
import com.budgetapp.domain.repository.IExpenseRepository
import com.budgetapp.domain.repository.IIncomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetBudgetSummaryUseCase @Inject constructor(
    private val incomeRepo: IIncomeRepository,
    private val expenseRepo: IExpenseRepository
) {
    operator fun invoke(period: DateRange): Flow<BudgetSummary> =
        combine(
            incomeRepo.getIncomeByDateRange(period.start, period.end),
            expenseRepo.getExpensesByDateRange(period.start, period.end),
            expenseRepo.getSpendingByCategory(period.start, period.end)
        ) { incomeList, _, spendingByCategory ->
            val totalIncome   = incomeList.sumOf { it.amount }
            val totalExpenses = spendingByCategory.values.sum()
            val topCategory   = spendingByCategory.maxByOrNull { it.value }?.key
            BudgetSummary(
                totalIncome         = totalIncome,
                totalExpenses       = totalExpenses,
                netBalance          = totalIncome - totalExpenses,
                savingsRate         = if (totalIncome > 0)
                    ((totalIncome - totalExpenses) / totalIncome).toFloat().coerceIn(-1f, 1f)
                else 0f,
                topSpendingCategory = topCategory,
                period              = period
            )
        }
}
