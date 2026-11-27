package com.example.opencalltracker.ui.history.components

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.opencalltracker.R
import com.example.opencalltracker.domain.model.HandledCall
import com.example.opencalltracker.domain.model.HandledCallOutcome

@Composable
fun HandledCallRow(handledCall: HandledCall, modifier: Modifier = Modifier) {
    val formattedDate = DateUtils.getRelativeDateTimeString(
        LocalContext.current,
        handledCall.handledAt,
        DateUtils.MINUTE_IN_MILLIS,
        DateUtils.WEEK_IN_MILLIS,
        0
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = RowVerticalPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = handledCall.phoneNumber ?: stringResource(R.string.history_unknown_number),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = formattedDate.toString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (handledCall.outcome == HandledCallOutcome.FAILED) {
            Text(
                text = stringResource(R.string.history_call_failed),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

private val RowVerticalPadding = 12.dp
