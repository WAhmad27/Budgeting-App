package com.budgetapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.budgetapp.ui.screen.analytics.AnalyticsScreen
import com.budgetapp.ui.screen.dashboard.DashboardScreen
import com.budgetapp.ui.screen.expense.AddEditExpenseScreen
import com.budgetapp.ui.screen.expense.ExpenseListScreen
import com.budgetapp.ui.screen.income.AddEditIncomeScreen
import com.budgetapp.ui.screen.income.IncomeListScreen
import com.budgetapp.ui.screen.insights.InsightsScreen
import com.budgetapp.ui.screen.savings.AddEditSavingsGoalScreen
import com.budgetapp.ui.screen.savings.SavingsScreen

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController    = navController,
        startDestination = NavRoutes.Dashboard.route,
        modifier         = modifier
    ) {
        composable(NavRoutes.Dashboard.route) {
            DashboardScreen(navController)
        }
        composable(NavRoutes.IncomeList.route) {
            IncomeListScreen(navController)
        }
        composable(
            route     = NavRoutes.AddIncome.route,
            arguments = listOf(navArgument("incomeId") {
                type     = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) {
            AddEditIncomeScreen(navController)
        }
        composable(NavRoutes.ExpenseList.route) {
            ExpenseListScreen(navController)
        }
        composable(
            route     = NavRoutes.AddExpense.route,
            arguments = listOf(navArgument("expenseId") {
                type     = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) {
            AddEditExpenseScreen(navController)
        }
        composable(NavRoutes.Savings.route) {
            SavingsScreen(navController)
        }
        composable(
            route     = NavRoutes.AddGoal.route,
            arguments = listOf(navArgument("goalId") {
                type     = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) {
            AddEditSavingsGoalScreen(navController)
        }
        composable(NavRoutes.Analytics.route) {
            AnalyticsScreen(navController)
        }
        composable(NavRoutes.Insights.route) {
            InsightsScreen(navController)
        }
    }
}
