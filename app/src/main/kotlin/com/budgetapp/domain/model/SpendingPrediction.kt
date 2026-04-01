package com.budgetapp.domain.model

data class SpendingPrediction(
    val category: Category,
    val predictedMonthlySpend: Double,
    val historicalAverage: Double,
    val trend: Trend,
    val confidenceLevel: Float
)

enum class Trend(val label: String) {
    INCREASING("Increasing"),
    STABLE("Stable"),
    DECREASING("Decreasing")
}
