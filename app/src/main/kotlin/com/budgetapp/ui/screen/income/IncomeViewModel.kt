package com.budgetapp.ui.screen.income

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.budgetapp.domain.model.Income
import com.budgetapp.domain.usecase.income.*
import com.budgetapp.ui.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IncomeViewModel @Inject constructor(
    private val getAllIncome: GetAllIncomeUseCase,
    private val addIncome: AddIncomeUseCase,
    private val updateIncome: UpdateIncomeUseCase,
    private val deleteIncome: DeleteIncomeUseCase,
) : ViewModel() {

    val incomeList: StateFlow<List<Income>> = getAllIncome()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    fun saveIncome(income: Income) = viewModelScope.launch {
        val result = if (income.id == 0L) addIncome(income) else updateIncome(income).map { 0L }
        result.fold(
            onSuccess = { _uiEvent.emit(UiEvent.NavigateBack) },
            onFailure = { _uiEvent.emit(UiEvent.ShowError(it.message ?: "Error saving income")) }
        )
    }

    fun deleteIncome(income: Income) = viewModelScope.launch {
        deleteIncome.invoke(income)
        _uiEvent.emit(UiEvent.ShowSnackbar("Income deleted"))
    }
}
