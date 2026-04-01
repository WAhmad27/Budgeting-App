package com.budgetapp.ui.screen.insights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.budgetapp.domain.model.SpendingInsight
import com.budgetapp.domain.model.SpendingPrediction
import com.budgetapp.domain.usecase.analytics.GetSpendingInsightsUseCase
import com.budgetapp.domain.usecase.analytics.GetSpendingPredictionUseCase
import com.budgetapp.ui.util.currentMonthRange
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class InsightsViewModel @Inject constructor(
    private val getInsights: GetSpendingInsightsUseCase,
    private val getPredictions: GetSpendingPredictionUseCase
) : ViewModel() {

    val insights: StateFlow<List<SpendingInsight>> = getInsights(currentMonthRange())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val predictions: StateFlow<List<SpendingPrediction>> = getPredictions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
