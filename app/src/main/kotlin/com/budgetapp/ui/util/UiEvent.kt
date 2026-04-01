package com.budgetapp.ui.util

sealed class UiEvent {
    object NavigateBack : UiEvent()
    data class ShowSnackbar(val message: String) : UiEvent()
    data class ShowError(val message: String) : UiEvent()
}
