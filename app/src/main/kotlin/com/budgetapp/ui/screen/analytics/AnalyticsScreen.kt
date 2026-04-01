package com.budgetapp.ui.screen.analytics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.budgetapp.domain.model.SpendingPrediction
import com.budgetapp.domain.model.Trend
import com.budgetapp.ui.components.charts.CategoryBarChart
import com.budgetapp.ui.components.charts.SpendingPieChart
import com.budgetapp.ui.components.charts.TrendLineChart
import com.budgetapp.ui.util.CurrencyFormatter
import com.budgetapp.ui.util.toComposeColor

@Composable
fun AnalyticsScreen(
    navController: NavController,
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val period          by viewModel.period.collectAsState()
    val summary         by viewModel.summary.collectAsState()
    val categorySpend   by viewModel.categorySpending.collectAsState()
    val predictions     by viewModel.predictions.collectAsState()
    val monthlyTrend    by viewModel.monthlyTrend.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Analytics") }) }
    ) { padding ->
        LazyColumn(
            modifier       = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Period selector
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AnalyticsPeriod.values().forEach { p ->
                        FilterChip(
                            selected = p == period,
                            onClick  = { viewModel.selectPeriod(p) },
                            label    = { Text(p.label) }
                        )
                    }
                }
            }

            // Summary row
            summary?.let { s ->
                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        SummaryTile("Income",   CurrencyFormatter.format(s.totalIncome),   com.budgetapp.ui.theme.IncomeGreen)
                        SummaryTile("Expenses", CurrencyFormatter.format(s.totalExpenses), com.budgetapp.ui.theme.ExpenseRed)
                        SummaryTile(
                            "Net",
                            CurrencyFormatter.format(s.netBalance),
                            if (s.netBalance >= 0) com.budgetapp.ui.theme.IncomeGreen else com.budgetapp.ui.theme.ExpenseRed
                        )
                    }
                }
            }

            // Trend line chart
            if (monthlyTrend.isNotEmpty()) {
                item {
                    SectionCard(title = "Income vs Expenses") {
                        TrendLineChart(
                            incomeData  = monthlyTrend.map { it.label to it.income },
                            expenseData = monthlyTrend.map { it.label to it.expenses },
                            modifier    = Modifier.fillMaxWidth().height(200.dp)
                        )
                    }
                }
            }

            // Pie chart
            if (categorySpend.isNotEmpty()) {
                item {
                    SectionCard(title = "Spending by Category") {
                        SpendingPieChart(
                            data     = categorySpend,
                            modifier = Modifier.fillMaxWidth().height(250.dp)
                        )
                    }
                }
                item {
                    SectionCard(title = "Top Categories") {
                        CategoryBarChart(
                            data     = categorySpend,
                            modifier = Modifier.fillMaxWidth().height(200.dp)
                        )
                    }
                }
            }

            // Predictions
            if (predictions.isNotEmpty()) {
                item {
                    Text(
                        "Next Month Predictions",
                        style      = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                items(predictions.take(5)) { prediction ->
                    PredictionCard(prediction)
                }
            }
        }
    }
}

@Composable
private fun SummaryTile(label: String, value: String, color: androidx.compose.ui.graphics.Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.titleSmall,
            color = color, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SectionCard(title: String, content: @Composable () -> Unit) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun PredictionCard(prediction: SpendingPrediction) {
    val color = prediction.category.colorHex.toComposeColor()
    val trendIcon = when (prediction.trend) {
        Trend.INCREASING -> "↑"
        Trend.DECREASING -> "↓"
        Trend.STABLE     -> "→"
    }
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier          = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(prediction.category.name, style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium)
                Text(
                    "Avg: ${CurrencyFormatter.format(prediction.historicalAverage)}/mo",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                "$trendIcon ${CurrencyFormatter.format(prediction.predictedMonthlySpend)}",
                style      = MaterialTheme.typography.titleSmall,
                color      = color,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
