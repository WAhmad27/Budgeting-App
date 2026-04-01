package com.budgetapp.ui.screen.income

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
import com.budgetapp.domain.model.Income
import com.budgetapp.ui.components.ConfirmDeleteDialog
import com.budgetapp.ui.components.EmptyStateView
import com.budgetapp.ui.components.TransactionItem
import com.budgetapp.ui.navigation.NavRoutes
import com.budgetapp.ui.theme.IncomeGreen
import com.budgetapp.ui.util.UiEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun IncomeListScreen(
    navController: NavController,
    viewModel: IncomeViewModel = hiltViewModel()
) {
    val incomeList by viewModel.incomeList.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var incomeToDelete by remember { mutableStateOf<Income?>(null) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
                else -> {}
            }
        }
    }

    incomeToDelete?.let { income ->
        ConfirmDeleteDialog(
            onConfirm = { viewModel.deleteIncome(income); incomeToDelete = null },
            onDismiss = { incomeToDelete = null }
        )
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Income") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(NavRoutes.AddIncome.withId(null))
            }) {
                Icon(Icons.Filled.Add, "Add Income")
            }
        }
    ) { padding ->
        if (incomeList.isEmpty()) {
            EmptyStateView(
                icon    = Icons.Filled.TrendingUp,
                message = "No income recorded yet.\nTap + to add one.",
                modifier = Modifier.padding(padding)
            )
        } else {
            LazyColumn(
                modifier       = Modifier.padding(padding),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(incomeList, key = { it.id }) { income ->
                    TransactionItem(
                        title      = income.title,
                        subtitle   = income.source,
                        amount     = income.amount,
                        date       = income.date,
                        accentColor = IncomeGreen,
                        isIncome   = true,
                        modifier   = Modifier
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = {
                            navController.navigate(NavRoutes.AddIncome.withId(income.id))
                        }) { Icon(Icons.Filled.Edit, "Edit", modifier = Modifier.size(18.dp)) }
                        IconButton(onClick = { incomeToDelete = income }) {
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
