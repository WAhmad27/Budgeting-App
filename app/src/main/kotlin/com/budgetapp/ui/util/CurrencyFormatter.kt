package com.budgetapp.ui.util

import java.text.NumberFormat
import java.util.Locale

object CurrencyFormatter {
    private val formatter = NumberFormat.getCurrencyInstance(Locale.US)

    fun format(amount: Double): String = formatter.format(amount)

    fun formatCompact(amount: Double): String = when {
        amount >= 1_000_000 -> "$%.1fM".format(amount / 1_000_000)
        amount >= 1_000     -> "$%.1fK".format(amount / 1_000)
        else                -> format(amount)
    }
}
