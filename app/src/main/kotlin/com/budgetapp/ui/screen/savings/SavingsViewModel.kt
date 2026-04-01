package com.budgetapp.ui.screen.savings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.budgetapp.domain.model.SavingsGoal
import com.budgetapp.domain.usecase.savings.*
import com.budgetapp.ui.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavingsViewModel @Inject constructor(
    private val addGoal: AddSavingsGoalUseCase,
    private val updateGoal: UpdateSavingsGoalUseCase,
    private val deleteGoal: DeleteSavingsGoalUseCase,
    private val contributeToGoal: ContributeToGoalUseCase,
    private val goalRepo: com.budgetapp.domain.repository.ISavingsGoalRepository
) : ViewModel() {

    val goals: StateFlow<List<SavingsGoal>> = goalRepo.getAllGoals()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    fun saveGoal(goal: SavingsGoal) = viewModelScope.launch {
        val result = if (goal.id == 0L) addGoal(goal) else updateGoal(goal).map { 0L }
        result.fold(
            onSuccess = { _uiEvent.emit(UiEvent.NavigateBack) },
            onFailure = { _uiEvent.emit(UiEvent.ShowError(it.message ?: "Error saving goal")) }
        )
    }

    fun contribute(goal: SavingsGoal, amount: Double) = viewModelScope.launch {
        contributeToGoal(goal, amount).fold(
            onSuccess = {
                val msg = if ((goal.currentAmount + amount) >= goal.targetAmount)
                    "Goal completed!" else "Funds added!"
                _uiEvent.emit(UiEvent.ShowSnackbar(msg))
            },
            onFailure = { _uiEvent.emit(UiEvent.ShowError(it.message ?: "Error")) }
        )
    }

    fun deleteGoal(goal: SavingsGoal) = viewModelScope.launch {
        deleteGoal.invoke(goal)
        _uiEvent.emit(UiEvent.ShowSnackbar("Goal deleted"))
    }
}
