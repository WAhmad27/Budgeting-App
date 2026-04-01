package com.budgetapp.domain.usecase.analytics

import com.budgetapp.domain.model.DateRange
import com.budgetapp.domain.model.InsightSeverity
import com.budgetapp.domain.model.SpendingInsight
import com.budgetapp.domain.repository.IExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

class GetSpendingInsightsUseCase @Inject constructor(
    private val expenseRepo: IExpenseRepository
) {
    operator fun invoke(period: DateRange): Flow<List<SpendingInsight>> {
        val prevStart = period.start.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth())
        val prevEnd   = period.start.minusDays(1)

        return combine(
            expenseRepo.getSpendingByCategory(period.start, period.end),
            expenseRepo.getSpendingByCategory(prevStart, prevEnd)
        ) { current, previous ->
            val allCategories = (current.keys + previous.keys).distinct()
            val totalCurrentSpend = current.values.sum()

            allCategories.mapNotNull { category ->
                val currentSpend  = current[category] ?: 0.0
                val previousSpend = previous[category] ?: 0.0

                if (currentSpend == 0.0 && previousSpend == 0.0) return@mapNotNull null

                val percentChange = if (previousSpend > 0)
                    ((currentSpend - previousSpend) / previousSpend * 100).toFloat()
                else if (currentSpend > 0) 100f
                else 0f

                val budgetLimit = category.monthlyBudgetLimit
                val budgetUsedPercent = budgetLimit?.let {
                    (currentSpend / it * 100).toFloat()
                }
                val shareOfTotal = if (totalCurrentSpend > 0)
                    (currentSpend / totalCurrentSpend * 100).toFloat() else 0f

                val (message, severity) = buildInsight(
                    category.name, currentSpend, percentChange,
                    budgetLimit, budgetUsedPercent, shareOfTotal
                )

                SpendingInsight(
                    category           = category,
                    currentSpend       = currentSpend,
                    previousPeriodSpend = previousSpend,
                    percentChange      = percentChange,
                    budgetLimit        = budgetLimit,
                    budgetUsedPercent  = budgetUsedPercent,
                    insightMessage     = message,
                    severity           = severity
                )
            }.sortedByDescending { it.severity.ordinal }
        }
    }

    private fun buildInsight(
        name: String,
        currentSpend: Double,
        percentChange: Float,
        budgetLimit: Double?,
        budgetUsedPercent: Float?,
        shareOfTotal: Float
    ): Pair<String, InsightSeverity> {
        if (budgetUsedPercent != null && budgetUsedPercent >= 100f) {
            val overage = currentSpend - (budgetLimit ?: currentSpend)
            return "You've exceeded your $name budget by \$%.2f".format(overage) to InsightSeverity.ALERT
        }
        if (budgetUsedPercent != null && budgetUsedPercent >= 80f) {
            return "You've used ${budgetUsedPercent.toInt()}% of your $name budget" to InsightSeverity.WARNING
        }
        if (percentChange >= 30f) {
            return "$name spending jumped ${percentChange.toInt()}% vs last month" to InsightSeverity.WARNING
        }
        if (percentChange >= 10f) {
            return "$name spend is up ${percentChange.toInt()}% vs last month" to InsightSeverity.INFO
        }
        if (shareOfTotal >= 40f) {
            return "$name is ${shareOfTotal.toInt()}% of your total spending this month" to InsightSeverity.INFO
        }
        return if (percentChange <= -10f)
            "$name spend is down ${(-percentChange).toInt()}% vs last month — great job!" to InsightSeverity.INFO
        else
            "$name spending is on track this month" to InsightSeverity.INFO
    }
}
