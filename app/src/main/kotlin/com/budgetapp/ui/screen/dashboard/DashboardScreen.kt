package com.budgetapp.ui.screen.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.budgetapp.domain.model.BudgetSummary
import com.budgetapp.domain.model.InsightSeverity
import com.budgetapp.domain.model.SpendingInsight
import com.budgetapp.ui.components.BudgetCard
import com.budgetapp.ui.components.charts.SpendingPieChart
import com.budgetapp.ui.navigation.NavRoutes
import com.budgetapp.ui.theme.ExpenseRed
import com.budgetapp.ui.theme.IncomeGreen
import com.budgetapp.ui.util.CurrencyFormatter
import com.budgetapp.ui.util.currentMonthRange
import com.budgetapp.ui.util.lastMonthRange
import com.budgetapp.ui.util.toComposeColor

@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val summary by viewModel.budgetSummary.collectAsState()
    val insights by viewModel.insights.collectAsState()
    val period by viewModel.selectedPeriod.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") },
                actions = {
                    IconButton(onClick = { navController.navigate(NavRoutes.Insights.route) }) {
                        Icon(Icons.Filled.Lightbulb, "Insights")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(NavRoutes.AddExpense.withId(null))
            }) {
                Icon(Icons.Filled.Add, "Add Expense")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier            = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding      = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Period selector
            item {
                PeriodSelector(
                    currentPeriod  = period,
                    onThisMonth    = { viewModel.selectPeriod(currentMonthRange()) },
                    onLastMonth    = { viewModel.selectPeriod(lastMonthRange()) }
                )
            }

            // Summary card
            item {
                summary?.let { SummaryCard(it) }
                    ?: CircularProgressIndicator(modifier = Modifier.padding(32.dp))
            }

            // Spending breakdown chart
            item {
                summary?.let { s ->
                    if (s.totalExpenses > 0) {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Spending by Category", style = MaterialTheme.typography.titleMedium)
                                Spacer(Modifier.height(8.dp))
                                // Pass the top-category as a representative entry for the chart
                                // Full category breakdown is on Analytics screen
                                Text(
                                    "Top category: ${s.topSpendingCategory?.name ?: "N/A"}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(Modifier.height(4.dp))
                                TextButton(onClick = { navController.navigate(NavRoutes.Analytics.route) }) {
                                    Text("View full breakdown →")
                                }
                            }
                        }
                    }
                }
            }

            // Top insights
            if (insights.isNotEmpty()) {
                item {
                    Text(
                        "Top Insights",
                        style      = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                items(insights.take(3)) { insight ->
                    InsightChip(insight = insight, onClick = {
                        navController.navigate(NavRoutes.Insights.route)
                    })
                }
            }
        }
    }
}

@Composable
private fun PeriodSelector(
    currentPeriod: com.budgetapp.domain.model.DateRange,
    onThisMonth: () -> Unit,
    onLastMonth: () -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(
            selected = currentPeriod == currentMonthRange(),
            onClick  = onThisMonth,
            label    = { Text("This Month") }
        )
        FilterChip(
            selected = currentPeriod == lastMonthRange(),
            onClick  = onLastMonth,
            label    = { Text("Last Month") }
        )
    }
}

@Composable
private fun SummaryCard(summary: BudgetSummary) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Financial Summary", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                SummaryItem("Income",   CurrencyFormatter.format(summary.totalIncome),   IncomeGreen)
                SummaryItem("Expenses", CurrencyFormatter.format(summary.totalExpenses), ExpenseRed)
                SummaryItem(
                    "Balance",
                    CurrencyFormatter.format(summary.netBalance),
                    if (summary.netBalance >= 0) IncomeGreen else ExpenseRed
                )
            }
            Spacer(Modifier.height(12.dp))
            LinearProgressIndicator(
                progress  = { summary.savingsRate.coerceIn(0f, 1f) },
                modifier  = Modifier.fillMaxWidth().height(6.dp),
                color     = IncomeGreen
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Savings rate: ${(summary.savingsRate * 100).toInt()}%",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SummaryItem(label: String, value: String, valueColor: androidx.compose.ui.graphics.Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(4.dp))
        Text(value, style = MaterialTheme.typography.titleSmall, color = valueColor, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun InsightChip(insight: SpendingInsight, onClick: () -> Unit) {
    val (containerColor, iconTint) = when (insight.severity) {
        InsightSeverity.ALERT   -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.error
        InsightSeverity.WARNING -> MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.tertiary
        InsightSeverity.INFO    -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.secondary
    }
    Surface(
        color    = containerColor,
        shape    = MaterialTheme.shapes.medium,
        onClick  = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier          = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector        = when (insight.severity) {
                    InsightSeverity.ALERT   -> Icons.Filled.Warning
                    InsightSeverity.WARNING -> Icons.Filled.Info
                    InsightSeverity.INFO    -> Icons.Filled.TipsAndUpdates
                },
                contentDescription = null,
                tint               = iconTint,
                modifier           = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text  = insight.insightMessage,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
