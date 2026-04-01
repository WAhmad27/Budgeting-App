package com.budgetapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    DASHBOARD ("dashboard",    "Dashboard", Icons.Filled.Home),
    EXPENSES  ("expense_list", "Expenses",  Icons.Filled.Receipt),
    INCOME    ("income_list",  "Income",    Icons.Filled.TrendingUp),
    SAVINGS   ("savings",      "Savings",   Icons.Filled.Savings),
    ANALYTICS ("analytics",    "Analytics", Icons.Filled.BarChart)
}
