package com.budgetapp.ui.screen.savings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.budgetapp.domain.model.SavingsGoal
import com.budgetapp.ui.components.ConfirmDeleteDialog
import com.budgetapp.ui.components.EmptyStateView
import com.budgetapp.ui.components.SavingsProgressBar
import com.budgetapp.ui.navigation.NavRoutes
import com.budgetapp.ui.util.CurrencyFormatter
import com.budgetapp.ui.util.UiEvent
import com.budgetapp.ui.util.toComposeColor
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingsScreen(
    navController: NavController,
    viewModel: SavingsViewModel = hiltViewModel()
) {
    val goals by viewModel.goals.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var goalToDelete by remember { mutableStateOf<SavingsGoal?>(null) }
    var contributeGoal by remember { mutableStateOf<SavingsGoal?>(null) }
    var contributeAmount by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
                is UiEvent.ShowError    -> snackbarHostState.showSnackbar(event.message)
                else -> {}
            }
        }
    }

    goalToDelete?.let { goal ->
        ConfirmDeleteDialog(
            onConfirm = { viewModel.deleteGoal(goal); goalToDelete = null },
            onDismiss = { goalToDelete = null }
        )
    }

    // Contribute bottom sheet
    contributeGoal?.let { goal ->
        AlertDialog(
            onDismissRequest = { contributeGoal = null; contributeAmount = "" },
            title   = { Text("Add Funds to ${goal.name}") },
            text    = {
                OutlinedTextField(
                    value         = contributeAmount,
                    onValueChange = { contributeAmount = it.filter { c -> c.isDigit() || c == '.' } },
                    label         = { Text("Amount") },
                    prefix        = { Text("$") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine    = true
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val amt = contributeAmount.toDoubleOrNull()
                        if (amt != null && amt > 0) {
                            viewModel.contribute(goal, amt)
                        }
                        contributeGoal = null
                        contributeAmount = ""
                    }
                ) { Text("Add") }
            },
            dismissButton = {
                TextButton(onClick = { contributeGoal = null; contributeAmount = "" }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Savings Goals") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(NavRoutes.AddGoal.withId(null))
            }) {
                Icon(Icons.Filled.Add, "Add Goal")
            }
        }
    ) { padding ->
        if (goals.isEmpty()) {
            EmptyStateView(
                icon     = Icons.Filled.Savings,
                message  = "No savings goals yet.\nTap + to create one.",
                modifier = Modifier.padding(padding)
            )
        } else {
            LazyColumn(
                modifier       = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(goals.filter { !it.isCompleted }, key = { it.id }) { goal ->
                    GoalCard(
                        goal       = goal,
                        onContribute = { contributeGoal = goal },
                        onEdit     = { navController.navigate(NavRoutes.AddGoal.withId(goal.id)) },
                        onDelete   = { goalToDelete = goal }
                    )
                }
                val completed = goals.filter { it.isCompleted }
                if (completed.isNotEmpty()) {
                    item {
                        Text(
                            "Completed Goals",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    items(completed, key = { it.id }) { goal ->
                        GoalCard(
                            goal = goal,
                            onContribute = {},
                            onEdit   = {},
                            onDelete = { goalToDelete = goal }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GoalCard(
    goal: SavingsGoal,
    onContribute: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val color = goal.colorHex.toComposeColor()
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text      = goal.name,
                    style     = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier  = Modifier.weight(1f)
                )
                if (goal.isCompleted) {
                    Icon(Icons.Filled.CheckCircle, "Completed", tint = color, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    CurrencyFormatter.format(goal.currentAmount),
                    style = MaterialTheme.typography.bodyLarge,
                    color = color,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "of ${CurrencyFormatter.format(goal.targetAmount)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.height(8.dp))
            SavingsProgressBar(progress = goal.progressPercent, color = color)
            goal.daysRemaining?.let { days ->
                Spacer(Modifier.height(4.dp))
                Text(
                    text  = if (days >= 0) "$days days remaining" else "Overdue by ${-days} days",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (days < 0) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (!goal.isCompleted) {
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = onContribute, modifier = Modifier.weight(1f)) {
                        Text("Add Funds")
                    }
                    IconButton(onClick = onEdit) { Icon(Icons.Filled.Edit, "Edit") }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Filled.Delete, "Delete", tint = MaterialTheme.colorScheme.error)
                    }
                }
            } else {
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Filled.Delete, "Delete", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}
