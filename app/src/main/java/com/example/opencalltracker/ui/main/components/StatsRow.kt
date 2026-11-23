package com.example.opencalltracker.ui.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.opencalltracker.R

@Composable
fun StatsRow(handledCallsToday: Int, handledCallsTotal: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(CardSpacing)
    ) {
        StatCard(
            value = handledCallsToday.toString(),
            label = stringResource(R.string.main_stat_today),
            modifier = Modifier.weight(1f)
        )
        StatCard(
            value = handledCallsTotal.toString(),
            label = stringResource(R.string.main_stat_total),
            modifier = Modifier.weight(1f)
        )
    }
}

private val CardSpacing = 12.dp
