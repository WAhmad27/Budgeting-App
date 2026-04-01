package com.budgetapp.ui.screen.savings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.budgetapp.domain.model.SavingsGoal
import com.budgetapp.ui.components.AmountTextField
import com.budgetapp.ui.util.UiEvent
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate

@Composable
fun AddEditSavingsGoalScreen(
    navController: NavController,
    viewModel: SavingsViewModel = hiltViewModel()
) {
    var name         by remember { mutableStateOf("") }
    var targetAmount by remember { mutableStateOf("") }

    val isFormValid = name.isNotBlank() && targetAmount.toDoubleOrNull()?.let { it > 0 } == true

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is UiEvent.NavigateBack -> navController.popBackStack()
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Savings Goal") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value         = name,
                onValueChange = { name = it },
                label         = { Text("Goal Name (e.g. Emergency Fund)") },
                singleLine    = true,
                modifier      = Modifier.fillMaxWidth()
            )
            AmountTextField(
                value         = targetAmount,
                onValueChange = { targetAmount = it },
                label         = "Target Amount",
                modifier      = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    viewModel.saveGoal(
                        SavingsGoal(
                            name          = name.trim(),
                            targetAmount  = targetAmount.toDouble(),
                            currentAmount = 0.0,
                            targetDate    = null,
                            iconName      = "savings",
                            colorHex      = "#1E7A5E",
                            createdDate   = LocalDate.now()
                        )
                    )
                },
                enabled  = isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Create Goal") }
        }
    }
}
