package com.example.opencalltracker.ui.main.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import java.util.Locale

@Composable
fun ActiveDuration(seconds: Long, modifier: Modifier = Modifier) {
    Text(
        text = formatDuration(seconds),
        style = MaterialTheme.typography.titleMedium,
        fontFamily = FontFamily.Monospace,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
    )
}

private fun formatDuration(totalSeconds: Long): String {
    val hours = totalSeconds / SECONDS_PER_HOUR
    val minutes = (totalSeconds % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE
    val seconds = totalSeconds % SECONDS_PER_MINUTE
    return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds)
}

private const val SECONDS_PER_HOUR = 3600L
private const val SECONDS_PER_MINUTE = 60L
