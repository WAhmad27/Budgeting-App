package com.budgetapp.domain.model

data class SpendingInsight(
    val category: Category,
    val currentSpend: Double,
    val previousPeriodSpend: Double,
    val percentChange: Float,
    val budgetLimit: Double?,
    val budgetUsedPercent: Float?,
    val insightMessage: String,
    val severity: InsightSeverity
)

enum class InsightSeverity { INFO, WARNING, ALERT }
