package com.budgetapp.ui.util

import com.budgetapp.domain.model.DateRange
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

fun currentMonthRange(): DateRange {
    val today = LocalDate.now()
    return DateRange(
        start = today.with(TemporalAdjusters.firstDayOfMonth()),
        end   = today.with(TemporalAdjusters.lastDayOfMonth())
    )
}

fun lastMonthRange(): DateRange {
    val lastMonth = LocalDate.now().minusMonths(1)
    return DateRange(
        start = lastMonth.with(TemporalAdjusters.firstDayOfMonth()),
        end   = lastMonth.with(TemporalAdjusters.lastDayOfMonth())
    )
}

fun last3MonthsRange(): DateRange {
    val today = LocalDate.now()
    return DateRange(
        start = today.minusMonths(3).with(TemporalAdjusters.firstDayOfMonth()),
        end   = today.with(TemporalAdjusters.lastDayOfMonth())
    )
}

fun last6MonthsRange(): DateRange {
    val today = LocalDate.now()
    return DateRange(
        start = today.minusMonths(6).with(TemporalAdjusters.firstDayOfMonth()),
        end   = today.with(TemporalAdjusters.lastDayOfMonth())
    )
}

fun lastYearRange(): DateRange {
    val today = LocalDate.now()
    return DateRange(
        start = today.minusYears(1).with(TemporalAdjusters.firstDayOfMonth()),
        end   = today.with(TemporalAdjusters.lastDayOfMonth())
    )
}
