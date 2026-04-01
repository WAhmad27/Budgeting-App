package com.budgetapp.ui.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.budgetapp.domain.model.BudgetSummary
import com.budgetapp.domain.model.DateRange
import com.budgetapp.domain.model.SpendingInsight
import com.budgetapp.domain.usecase.analytics.GetBudgetSummaryUseCase
import com.budgetapp.domain.usecase.analytics.GetSpendingInsightsUseCase
import com.budgetapp.ui.util.currentMonthRange
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getBudgetSummary: GetBudgetSummaryUseCase,
    private val getInsights: GetSpendingInsightsUseCase
) : ViewModel() {

    private val _selectedPeriod = MutableStateFlow(currentMonthRange())
    val selectedPeriod: StateFlow<DateRange> = _selectedPeriod.asStateFlow()

    val budgetSummary: StateFlow<BudgetSummary?> = _selectedPeriod
        .flatMapLatest { getBudgetSummary(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val insights: StateFlow<List<SpendingInsight>> = _selectedPeriod
        .flatMapLatest { getInsights(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun selectPeriod(range: DateRange) { _selectedPeriod.value = range }
}
