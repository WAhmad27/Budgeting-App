package com.budgetapp.ui.components.charts

import android.graphics.Color as AndroidColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.budgetapp.domain.model.Category
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

@Composable
fun SpendingPieChart(
    data: Map<Category, Double>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) return

    AndroidView(
        factory = { ctx ->
            PieChart(ctx).apply {
                description.isEnabled = false
                isDrawHoleEnabled     = true
                holeRadius            = 55f
                transparentCircleRadius = 58f
                setHoleColor(AndroidColor.TRANSPARENT)
                setUsePercentValues(true)
                legend.isEnabled      = true
                setEntryLabelColor(AndroidColor.WHITE)
                setEntryLabelTextSize(11f)
            }
        },
        update = { chart ->
            val entries = data.entries
                .filter { it.value > 0 }
                .map { (cat, amount) -> PieEntry(amount.toFloat(), cat.name) }
            val colors = data.keys
                .filter { (data[it] ?: 0.0) > 0 }
                .map { cat ->
                    try { AndroidColor.parseColor(cat.colorHex) }
                    catch (e: Exception) { AndroidColor.GRAY }
                }
            val dataSet = PieDataSet(entries, "").apply {
                this.colors = colors
                valueTextColor = AndroidColor.WHITE
                valueTextSize  = 11f
                sliceSpace     = 2f
            }
            chart.data = PieData(dataSet)
            chart.invalidate()
        },
        modifier = modifier
    )
}
