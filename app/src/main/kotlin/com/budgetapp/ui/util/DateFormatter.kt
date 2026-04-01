package com.budgetapp.ui.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object DateFormatter {
    private val medium  = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    private val short   = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
    private val monthYear = DateTimeFormatter.ofPattern("MMM yyyy")

    fun format(date: LocalDate): String          = date.format(medium)
    fun formatShort(date: LocalDate): String     = date.format(short)
    fun formatMonthYear(date: LocalDate): String = date.format(monthYear)
}
