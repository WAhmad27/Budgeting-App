package com.budgetapp.ui.screen.expense

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.budgetapp.domain.model.Category
import com.budgetapp.domain.model.Expense
import com.budgetapp.domain.model.RecurrenceInterval
import com.budgetapp.ui.components.AmountTextField
import com.budgetapp.ui.components.CategoryChip
import com.budgetapp.ui.util.UiEvent
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate

@Composable
fun AddEditExpenseScreen(
    navController: NavController,
    viewModel: ExpenseViewModel = hiltViewModel()
) {
    val categories by viewModel.categories.collectAsState()

    var title       by remember { mutableStateOf("") }
    var amount      by remember { mutableStateOf("") }
    var notes       by remember { mutableStateOf("") }
    var selectedCat by remember { mutableStateOf<Category?>(null) }
    var isRecurring by remember { mutableStateOf(false) }
    var interval    by remember { mutableStateOf(RecurrenceInterval.MONTHLY) }
    var showIntervalMenu by remember { mutableStateOf(false) }

    // Auto-select first category when loaded
    LaunchedEffect(categories) {
        if (selectedCat == null && categories.isNotEmpty()) {
            selectedCat = categories.first()
        }
    }

    val isFormValid = title.isNotBlank() &&
            amount.toDoubleOrNull()?.let { it > 0 } == true &&
            selectedCat != null

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
                title = { Text("Add Expense") },
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

            // Category selection
            Text("Category", style = MaterialTheme.typography.titleSmall)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    CategoryChip(
                        category = cat,
                        selected = cat == selectedCat,
                        onClick  = { selectedCat = cat }
                    )
                }
            }

            OutlinedTextField(
                value         = notes,
                onValueChange = { notes = it },
                label         = { Text("Notes (optional)") },
                minLines      = 2,
                modifier      = Modifier.fillMaxWidth()
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
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
                    selectedCat?.let { cat ->
                        viewModel.saveExpense(
                            Expense(
                                title              = title.trim(),
                                amount             = amount.toDouble(),
                                category           = cat,
                                date               = LocalDate.now(),
                                notes              = notes.trim().ifBlank { null },
                                isRecurring        = isRecurring,
                                recurrenceInterval = if (isRecurring) interval else null
                            )
                        )
                    }
                },
                enabled  = isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Save Expense") }
        }
    }
}
