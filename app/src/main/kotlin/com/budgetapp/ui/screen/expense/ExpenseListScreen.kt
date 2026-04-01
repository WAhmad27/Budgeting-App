package com.budgetapp.ui.screen.expense

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.budgetapp.domain.model.Expense
import com.budgetapp.ui.components.ConfirmDeleteDialog
import com.budgetapp.ui.components.EmptyStateView
import com.budgetapp.ui.components.TransactionItem
import com.budgetapp.ui.navigation.NavRoutes
import com.budgetapp.ui.util.DateFormatter
import com.budgetapp.ui.util.UiEvent
import com.budgetapp.ui.util.toComposeColor
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate

@Composable
fun ExpenseListScreen(
    navController: NavController,
    viewModel: ExpenseViewModel = hiltViewModel()
) {
    val expenses by viewModel.expenses.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var expenseToDelete by remember { mutableStateOf<Expense?>(null) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
                else -> {}
            }
        }
    }

    expenseToDelete?.let { expense ->
        ConfirmDeleteDialog(
            onConfirm = { viewModel.deleteExpense(expense); expenseToDelete = null },
            onDismiss = { expenseToDelete = null }
        )
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Expenses") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(NavRoutes.AddExpense.withId(null))
            }) {
                Icon(Icons.Filled.Add, "Add Expense")
            }
        }
    ) { padding ->
        if (expenses.isEmpty()) {
            EmptyStateView(
                icon    = Icons.Filled.Receipt,
                message = "No expenses yet.\nTap + to add one.",
                modifier = Modifier.padding(padding)
            )
        } else {
            val grouped = expenses.groupBy { it.date }
            LazyColumn(
                modifier       = Modifier.padding(padding),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                grouped.entries.sortedByDescending { it.key }.forEach { (date, dayExpenses) ->
                    item {
                        Text(
                            text     = DateFormatter.format(date),
                            style    = MaterialTheme.typography.labelLarge,
                            color    = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }
                    items(dayExpenses, key = { it.id }) { expense ->
                        TransactionItem(
                            title       = expense.title,
                            subtitle    = expense.category.name,
                            amount      = expense.amount,
                            date        = expense.date,
                            accentColor = expense.category.colorHex.toComposeColor()
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 8.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(onClick = {
                                navController.navigate(NavRoutes.AddExpense.withId(expense.id))
                            }) { Icon(Icons.Filled.Edit, "Edit", modifier = Modifier.size(18.dp)) }
                            IconButton(onClick = { expenseToDelete = expense }) {
                                Icon(Icons.Filled.Delete, "Delete", modifier = Modifier.size(18.dp),
                                    tint = MaterialTheme.colorScheme.error)
                            }
                        }
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }
        }
    }
}
