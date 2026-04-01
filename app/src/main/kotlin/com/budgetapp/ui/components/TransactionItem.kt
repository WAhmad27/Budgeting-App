package com.budgetapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.budgetapp.ui.theme.ExpenseRed
import com.budgetapp.ui.theme.IncomeGreen
import com.budgetapp.ui.util.CurrencyFormatter
import com.budgetapp.ui.util.DateFormatter
import java.time.LocalDate

@Composable
fun TransactionItem(
    title: String,
    subtitle: String,
    amount: Double,
    date: LocalDate,
    accentColor: Color,
    isIncome: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(
        modifier        = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(accentColor, CircleShape)
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text  = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text  = "$subtitle · ${DateFormatter.formatShort(date)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text  = (if (isIncome) "+" else "-") + CurrencyFormatter.format(amount),
            style = MaterialTheme.typography.titleMedium,
            color = if (isIncome) IncomeGreen else ExpenseRed,
            fontWeight = FontWeight.SemiBold
        )
    }
}
