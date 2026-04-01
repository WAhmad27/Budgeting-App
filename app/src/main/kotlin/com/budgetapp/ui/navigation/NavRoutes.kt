package com.budgetapp.ui.navigation

sealed class NavRoutes(val route: String) {
    object Dashboard   : NavRoutes("dashboard")
    object IncomeList  : NavRoutes("income_list")
    object AddIncome   : NavRoutes("add_income?incomeId={incomeId}") {
        fun withId(id: Long?) = "add_income?incomeId=$id"
    }
    object ExpenseList : NavRoutes("expense_list")
    object AddExpense  : NavRoutes("add_expense?expenseId={expenseId}") {
        fun withId(id: Long?) = "add_expense?expenseId=$id"
    }
    object Savings     : NavRoutes("savings")
    object AddGoal     : NavRoutes("add_goal?goalId={goalId}") {
        fun withId(id: Long?) = "add_goal?goalId=$id"
    }
    object Analytics   : NavRoutes("analytics")
    object Insights    : NavRoutes("insights")
}
