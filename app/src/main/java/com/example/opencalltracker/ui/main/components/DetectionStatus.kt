package com.example.opencalltracker.ui.main.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.opencalltracker.R

@Composable
fun DetectionStatus(isEnabled: Boolean, modifier: Modifier = Modifier) {
    val titleColor by animateColorAsState(
        targetValue = if (isEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
        label = "titleColor"
    )

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(if (isEnabled) R.string.main_status_active else R.string.main_status_inactive),
            style = MaterialTheme.typography.headlineSmall,
            color = titleColor
        )
        Spacer(modifier = Modifier.height(DescriptionSpacing))
        Text(
            text = stringResource(if (isEnabled) R.string.main_status_active_description else R.string.main_status_inactive_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

private val DescriptionSpacing = 8.dp
