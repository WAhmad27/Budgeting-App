package com.budgetapp.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SavingsProgressBar(
    progress: Float,
    color: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
) {
    var started by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { started = true }
    val animatedProgress by animateFloatAsState(
        targetValue = if (started) progress else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "progress"
    )
    Column(modifier = modifier) {
        LinearProgressIndicator(
            progress  = { animatedProgress },
            modifier  = Modifier.fillMaxWidth().height(8.dp),
            color     = color,
            trackColor = color.copy(alpha = 0.2f)
        )
        Spacer(Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text  = "${(animatedProgress * 100).toInt()}%",
                style = MaterialTheme.typography.labelSmall,
                color = color
            )
        }
    }
}
