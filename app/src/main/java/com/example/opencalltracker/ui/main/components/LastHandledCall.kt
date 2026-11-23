package com.example.opencalltracker.ui.main.components

import android.text.format.DateUtils
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.opencalltracker.R
import com.example.opencalltracker.domain.model.HandledCall

@Composable
fun LastHandledCall(handledCall: HandledCall, modifier: Modifier = Modifier) {
    val phoneNumber = handledCall.phoneNumber ?: stringResource(R.string.main_last_call_unknown_number)
    val relativeTime = DateUtils.getRelativeTimeSpanString(
        handledCall.handledAt,
        System.currentTimeMillis(),
        DateUtils.MINUTE_IN_MILLIS
    )

    Text(
        text = stringResource(R.string.main_last_call, phoneNumber, relativeTime),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}
