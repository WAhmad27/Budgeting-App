package com.budgetapp.ui.screen.insights

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.budgetapp.domain.model.InsightSeverity
import com.budgetapp.domain.model.SpendingInsight
import com.budgetapp.domain.model.SpendingPrediction
import com.budgetapp.domain.model.Trend
import com.budgetapp.ui.util.CurrencyFormatter
import com.budgetapp.ui.util.toComposeColor

@Composable
fun InsightsScreen(
    navController: NavController,
    viewModel: InsightsViewModel = hiltViewModel()
) {
    val insights    by viewModel.insights.collectAsState()
    val predictions by viewModel.predictions.collectAsState()

    val alerts   = insights.filter { it.severity == InsightSeverity.ALERT }
    val warnings = insights.filter { it.severity == InsightSeverity.WARNING }
    val infos    = insights.filter { it.severity == InsightSeverity.INFO }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Spending Insights") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier       = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (insights.isEmpty() && predictions.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text(
                            "Add some expenses to see insights.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            if (alerts.isNotEmpty()) {
                item { SectionHeader("Alerts", Icons.Filled.Warning, MaterialTheme.colorScheme.error) }
                items(alerts) { InsightCard(it) }
            }
            if (warnings.isNotEmpty()) {
                item { SectionHeader("Warnings", Icons.Filled.Info, MaterialTheme.colorScheme.tertiary) }
                items(warnings) { InsightCard(it) }
            }
            if (infos.isNotEmpty()) {
                item { SectionHeader("Info", Icons.Filled.TipsAndUpdates, MaterialTheme.colorScheme.primary) }
                items(infos) { InsightCard(it) }
            }

            // Where to cut spending
            val topOverspend = insights.filter { (it.budgetUsedPercent ?: 0f) > 80f || it.percentChange > 20f }
                .sortedByDescending { it.currentSpend }
            if (topOverspend.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Where to Cut Spending",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                items(topOverspend.take(3)) { insight ->
                    CutSpendingCard(insight)
                }
            }

            // Predictions
            if (predictions.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Spending Forecast",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                items(predictions.take(5)) { prediction ->
                    PredictionRow(prediction)
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, icon: ImageVector, color: androidx.compose.ui.graphics.Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Icon(icon, null, tint = color, modifier = Modifier.size(18.dp))
        Text(title, style = MaterialTheme.typography.titleSmall, color = color, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun InsightCard(insight: SpendingInsight) {
    val containerColor = when (insight.severity) {
        InsightSeverity.ALERT   -> MaterialTheme.colorScheme.errorContainer
        InsightSeverity.WARNING -> MaterialTheme.colorScheme.tertiaryContainer
        InsightSeverity.INFO    -> MaterialTheme.colorScheme.secondaryContainer
    }
    val catColor = insight.category.colorHex.toComposeColor()
    Surface(color = containerColor, shape = MaterialTheme.shapes.medium) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(12.dp)) {
                Surface(color = catColor, shape = MaterialTheme.shapes.small) { Box(Modifier.size(12.dp)) }
            }
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(insight.category.name, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                Text(insight.insightMessage, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.width(8.dp))
            Text(
                CurrencyFormatter.format(insight.currentSpend),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun CutSpendingCard(insight: SpendingInsight) {
    val saving = if (insight.budgetLimit != null)
        insight.currentSpend - insight.budgetLimit
    else
        insight.currentSpend * 0.2  // Suggest cutting 20%

    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Cut ${insight.category.name} spending", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(4.dp))
            Text(
                "Reducing ${insight.category.name} to recommended levels could save you " +
                        "${CurrencyFormatter.format(saving.coerceAtLeast(0.0))}/month.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun PredictionRow(prediction: SpendingPrediction) {
    val trendColor = when (prediction.trend) {
        Trend.INCREASING -> MaterialTheme.colorScheme.error
        Trend.DECREASING -> MaterialTheme.colorScheme.primary
        Trend.STABLE     -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    val trendSymbol = when (prediction.trend) {
        Trend.INCREASING -> "↑"
        Trend.DECREASING -> "↓"
        Trend.STABLE     -> "→"
    }
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(prediction.category.name, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
        Text(
            "$trendSymbol ${CurrencyFormatter.format(prediction.predictedMonthlySpend)}",
            style = MaterialTheme.typography.bodyMedium,
            color = trendColor,
            fontWeight = FontWeight.SemiBold
        )
    }
    HorizontalDivider()
}
