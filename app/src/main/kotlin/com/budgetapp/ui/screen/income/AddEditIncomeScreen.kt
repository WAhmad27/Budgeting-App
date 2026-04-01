package com.budgetapp.ui.screen.income

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
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import com.budgetapp.domain.model.Income
import com.budgetapp.domain.model.RecurrenceInterval
import com.budgetapp.ui.components.AmountTextField
import com.budgetapp.ui.util.UiEvent
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate

@Composable
fun AddEditIncomeScreen(
    navController: NavController,
    viewModel: IncomeViewModel = hiltViewModel()
) {
    var title       by remember { mutableStateOf("") }
    var amount      by remember { mutableStateOf("") }
    var source      by remember { mutableStateOf("Salary") }
    var notes       by remember { mutableStateOf("") }
    var isRecurring by remember { mutableStateOf(false) }
    var interval    by remember { mutableStateOf(RecurrenceInterval.MONTHLY) }
    var showIntervalMenu by remember { mutableStateOf(false) }

    val isFormValid = title.isNotBlank() && amount.toDoubleOrNull()?.let { it > 0 } == true

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
                title = { Text("Add Income") },
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
                value         = title,
                onValueChange = { title = it },
                label         = { Text("Title") },
                singleLine    = true,
                modifier      = Modifier.fillMaxWidth()
            )
            AmountTextField(
                value         = amount,
                onValueChange = { amount = it },
                modifier      = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value         = source,
                onValueChange = { source = it },
                label         = { Text("Source (e.g. Salary, Freelance)") },
                singleLine    = true,
                modifier      = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value         = notes,
                onValueChange = { notes = it },
                label         = { Text("Notes (optional)") },
                minLines      = 2,
                modifier      = Modifier.fillMaxWidth()
            )
            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Recurring", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                Switch(checked = isRecurring, onCheckedChange = { isRecurring = it })
            }
            if (isRecurring) {
                Box {
                    OutlinedButton(
                        onClick  = { showIntervalMenu = true },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Interval: ${interval.label}") }
                    DropdownMenu(
                        expanded         = showIntervalMenu,
                        onDismissRequest = { showIntervalMenu = false }
                    ) {
                        RecurrenceInterval.values().forEach { opt ->
                            DropdownMenuItem(
                                text    = { Text(opt.label) },
                                onClick = { interval = opt; showIntervalMenu = false }
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    viewModel.saveIncome(
                        Income(
                            title              = title.trim(),
                            amount             = amount.toDouble(),
                            source             = source.trim(),
                            isRecurring        = isRecurring,
                            recurrenceInterval = if (isRecurring) interval else null,
                            date               = LocalDate.now(),
                            notes              = notes.trim().ifBlank { null }
                        )
                    )
                },
                enabled  = isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Save Income") }
        }
    }
}
