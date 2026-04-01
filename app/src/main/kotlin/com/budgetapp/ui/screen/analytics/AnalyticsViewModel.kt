package com.budgetapp.ui.screen.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.budgetapp.domain.model.BudgetSummary
import com.budgetapp.domain.model.Category
import com.budgetapp.domain.model.SpendingPrediction
import com.budgetapp.domain.usecase.analytics.GetBudgetSummaryUseCase
import com.budgetapp.domain.usecase.analytics.GetSpendingPredictionUseCase
import com.budgetapp.domain.usecase.expense.GetExpensesByCategoryUseCase
import com.budgetapp.ui.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

data class MonthlyPoint(val label: String, val income: Double, val expenses: Double)

enum class AnalyticsPeriod(val label: String) {
    ONE_MONTH("1M"),
    THREE_MONTHS("3M"),
    SIX_MONTHS("6M"),
    ONE_YEAR("1Y")
}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val getBudgetSummary: GetBudgetSummaryUseCase,
    private val getPredictions: GetSpendingPredictionUseCase,
    private val getSpendingByCategory: GetExpensesByCategoryUseCase
) : ViewModel() {

    private val _period = MutableStateFlow(AnalyticsPeriod.THREE_MONTHS)
    val period: StateFlow<AnalyticsPeriod> = _period.asStateFlow()

    val dateRange = _period.map { p ->
        when (p) {
            AnalyticsPeriod.ONE_MONTH    -> currentMonthRange()
            AnalyticsPeriod.THREE_MONTHS -> last3MonthsRange()
            AnalyticsPeriod.SIX_MONTHS   -> last6MonthsRange()
            AnalyticsPeriod.ONE_YEAR     -> lastYearRange()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), currentMonthRange())

    val summary: StateFlow<BudgetSummary?> = dateRange
        .flatMapLatest { getBudgetSummary(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val categorySpending: StateFlow<Map<Category, Double>> = dateRange
        .flatMapLatest { getSpendingByCategory(it.start, it.end) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyMap())

    val predictions: StateFlow<List<SpendingPrediction>> = getPredictions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // Build per-month income/expense points for the trend chart (last 6 months)
    val monthlyTrend: StateFlow<List<MonthlyPoint>> = dateRange
        .flatMapLatest { range ->
            // Compute months in range
            val months = mutableListOf<Pair<LocalDate, LocalDate>>()
            var cursor = range.start.with(TemporalAdjusters.firstDayOfMonth())
            while (!cursor.isAfter(range.end)) {
                months.add(cursor to cursor.with(TemporalAdjusters.lastDayOfMonth()))
                cursor = cursor.plusMonths(1)
            }
            // Combine summaries for each month
            val flows = months.map { (start, end) ->
                getBudgetSummary(com.budgetapp.domain.model.DateRange(start, end))
                    .map { summary ->
                        MonthlyPoint(
                            label    = start.month.name.take(3) + " " + start.year.toString().takeLast(2),
                            income   = summary.totalIncome,
                            expenses = summary.totalExpenses
                        )
                    }
            }
            if (flows.isEmpty()) flowOf(emptyList())
            else combine(flows) { it.toList() }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun selectPeriod(p: AnalyticsPeriod) { _period.value = p }
}
