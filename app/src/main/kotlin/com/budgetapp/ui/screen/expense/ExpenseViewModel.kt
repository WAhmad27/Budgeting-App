package com.budgetapp.ui.screen.expense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.budgetapp.domain.model.Category
import com.budgetapp.domain.model.Expense
import com.budgetapp.domain.usecase.expense.*
import com.budgetapp.domain.repository.ICategoryRepository
import com.budgetapp.ui.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val getAllExpenses: GetAllExpensesUseCase,
    private val addExpense: AddExpenseUseCase,
    private val updateExpense: UpdateExpenseUseCase,
    private val deleteExpense: DeleteExpenseUseCase,
    private val categoryRepo: ICategoryRepository
) : ViewModel() {

    val expenses: StateFlow<List<Expense>> = getAllExpenses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val categories: StateFlow<List<Category>> = categoryRepo.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    fun saveExpense(expense: Expense) = viewModelScope.launch {
        val result = if (expense.id == 0L) addExpense(expense) else updateExpense(expense).map { 0L }
        result.fold(
            onSuccess = { _uiEvent.emit(UiEvent.NavigateBack) },
            onFailure = { _uiEvent.emit(UiEvent.ShowError(it.message ?: "Error saving expense")) }
        )
    }

    fun deleteExpense(expense: Expense) = viewModelScope.launch {
        deleteExpense.invoke(expense)
        _uiEvent.emit(UiEvent.ShowSnackbar("Expense deleted"))
    }
}
