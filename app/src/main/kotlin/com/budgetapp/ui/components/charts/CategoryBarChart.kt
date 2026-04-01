package com.budgetapp.ui.components.charts

import android.graphics.Color as AndroidColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.budgetapp.domain.model.Category
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

@Composable
fun CategoryBarChart(
    data: Map<Category, Double>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) return

    AndroidView(
        factory = { ctx ->
            BarChart(ctx).apply {
                description.isEnabled = false
                setDrawValueAboveBar(true)
                xAxis.position        = XAxis.XAxisPosition.BOTTOM
                xAxis.granularity     = 1f
                xAxis.setDrawGridLines(false)
                axisRight.isEnabled   = false
                legend.isEnabled      = false
                setFitBars(true)
            }
        },
        update = { chart ->
            val sorted = data.entries.sortedByDescending { it.value }.take(5)
            val labels = sorted.map { it.key.name }
            val colors = sorted.map { (cat, _) ->
                try { AndroidColor.parseColor(cat.colorHex) } catch (e: Exception) { AndroidColor.GRAY }
            }
            chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            chart.xAxis.labelRotationAngle = -30f

            val entries = sorted.mapIndexed { i, (_, v) -> BarEntry(i.toFloat(), v.toFloat()) }
            val dataSet = BarDataSet(entries, "Spending").apply {
                this.colors = colors
                valueTextSize = 10f
            }
            chart.data = BarData(dataSet)
            chart.invalidate()
        },
        modifier = modifier
    )
}
