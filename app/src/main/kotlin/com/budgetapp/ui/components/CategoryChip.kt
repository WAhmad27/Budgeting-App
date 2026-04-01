package com.budgetapp.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.budgetapp.domain.model.Category
import com.budgetapp.ui.util.toComposeColor

@Composable
fun CategoryChip(
    category: Category,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = category.colorHex.toComposeColor()
    val borderColor = if (selected) color else Color.Transparent
    val containerColor = if (selected) color.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant

    Surface(
        color  = containerColor,
        shape  = RoundedCornerShape(8.dp),
        modifier = modifier
            .border(2.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier            = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment   = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .also {
                        // Colored dot
                    }
            ) {
                Surface(color = color, shape = androidx.compose.foundation.shape.CircleShape) {
                    Box(modifier = Modifier.size(8.dp))
                }
            }
            Text(
                text  = category.name,
                style = MaterialTheme.typography.labelMedium,
                color = if (selected) color else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
