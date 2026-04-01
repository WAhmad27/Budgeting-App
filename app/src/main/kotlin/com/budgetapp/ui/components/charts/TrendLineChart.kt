package com.budgetapp.ui.components.charts

import android.graphics.Color as AndroidColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

@Composable
fun TrendLineChart(
    incomeData: List<Pair<String, Double>>,
    expenseData: List<Pair<String, Double>>,
    modifier: Modifier = Modifier
) {
    if (incomeData.isEmpty() && expenseData.isEmpty()) return

    AndroidView(
        factory = { ctx ->
            LineChart(ctx).apply {
                description.isEnabled = false
                setTouchEnabled(true)
                isDragEnabled   = true
                setScaleEnabled(false)
                xAxis.position  = XAxis.XAxisPosition.BOTTOM
                xAxis.granularity = 1f
                axisRight.isEnabled = false
                legend.isEnabled = true
            }
        },
        update = { chart ->
            val labels = incomeData.map { it.first }
            chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)

            val incomeEntries  = incomeData.mapIndexed  { i, (_, v) -> Entry(i.toFloat(), v.toFloat()) }
            val expenseEntries = expenseData.mapIndexed { i, (_, v) -> Entry(i.toFloat(), v.toFloat()) }

            val incomeSet = LineDataSet(incomeEntries, "Income").apply {
                color       = AndroidColor.parseColor("#4CAF50")
                lineWidth   = 2f
                setCircleColor(AndroidColor.parseColor("#4CAF50"))
                circleRadius = 4f
                setDrawValues(false)
                mode        = LineDataSet.Mode.CUBIC_BEZIER
            }
            val expenseSet = LineDataSet(expenseEntries, "Expenses").apply {
                color       = AndroidColor.parseColor("#F44336")
                lineWidth   = 2f
                setCircleColor(AndroidColor.parseColor("#F44336"))
                circleRadius = 4f
                setDrawValues(false)
                mode        = LineDataSet.Mode.CUBIC_BEZIER
            }
            chart.data = LineData(incomeSet, expenseSet)
            chart.invalidate()
        },
        modifier = modifier
    )
}
