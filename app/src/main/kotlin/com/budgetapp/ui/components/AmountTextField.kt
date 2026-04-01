package com.budgetapp.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun AmountTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "Amount",
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }

    OutlinedTextField(
        value        = value,
        onValueChange = { input ->
            // Allow digits and at most one decimal point with 2 decimal places
            val filtered = input.filter { it.isDigit() || it == '.' }
            val parts    = filtered.split('.')
            val clean = when {
                parts.size > 2      -> parts[0] + "." + parts[1]
                parts.size == 2     -> parts[0] + "." + parts[1].take(2)
                else                -> filtered
            }
            onValueChange(clean)
        },
        label         = { Text(label) },
        prefix        = { Text("$") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        singleLine    = true,
        modifier      = modifier.onFocusChanged { isFocused = it.isFocused }
    )
}
