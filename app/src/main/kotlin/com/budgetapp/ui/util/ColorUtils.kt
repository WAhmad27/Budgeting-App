package com.budgetapp.ui.util

import androidx.compose.ui.graphics.Color

fun String.toComposeColor(): Color = try {
    Color(android.graphics.Color.parseColor(this))
} catch (e: IllegalArgumentException) {
    Color.Gray
}
