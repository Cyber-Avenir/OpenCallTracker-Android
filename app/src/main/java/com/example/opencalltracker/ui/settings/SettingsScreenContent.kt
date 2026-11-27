package com.example.opencalltracker.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.opencalltracker.R
import com.example.opencalltracker.domain.model.ToneSettings
import com.example.opencalltracker.ui.settings.components.SettingStepperRow
import com.example.opencalltracker.ui.theme.OpenCallTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    toneSettings: ToneSettings,
    onToneDelayChanged: (Long) -> Unit,
    onToneDurationChanged: (Long) -> Unit,
    onToneRepeatChanged: (Int) -> Unit,
    onHangUpDelayChanged: (Long) -> Unit,
    onResetClicked: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.settings_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.settings_back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = HorizontalPadding, vertical = VerticalPadding),
            verticalArrangement = Arrangement.spacedBy(SectionSpacing)
        ) {
            Text(
                text = stringResource(R.string.settings_intro),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = DIVIDER_ALPHA))

            SettingStepperRow(
                label = stringResource(R.string.settings_tone_duration),
                description = stringResource(R.string.settings_tone_duration_description),
                value = stringResource(R.string.settings_milliseconds, toneSettings.toneDurationMillis),
                canDecrease = toneSettings.toneDurationMillis > MIN_DURATION_MILLIS,
                canIncrease = toneSettings.toneDurationMillis < MAX_DURATION_MILLIS,
                onDecrease = { onToneDurationChanged(-DURATION_STEP_MILLIS) },
                onIncrease = { onToneDurationChanged(DURATION_STEP_MILLIS) }
            )
            SettingStepperRow(
                label = stringResource(R.string.settings_tone_repeat),
                description = stringResource(R.string.settings_tone_repeat_description),
                value = stringResource(R.string.settings_times, toneSettings.toneRepeatCount),
                canDecrease = toneSettings.toneRepeatCount > MIN_REPEAT_COUNT,
                canIncrease = toneSettings.toneRepeatCount < MAX_REPEAT_COUNT,
                onDecrease = { onToneRepeatChanged(-REPEAT_STEP) },
                onIncrease = { onToneRepeatChanged(REPEAT_STEP) }
            )
            SettingStepperRow(
                label = stringResource(R.string.settings_tone_delay),
                description = stringResource(R.string.settings_tone_delay_description),
                value = stringResource(R.string.settings_milliseconds, toneSettings.toneDelayMillis),
                canDecrease = toneSettings.toneDelayMillis > MIN_DELAY_MILLIS,
                canIncrease = toneSettings.toneDelayMillis < MAX_DELAY_MILLIS,
                onDecrease = { onToneDelayChanged(-DELAY_STEP_MILLIS) },
                onIncrease = { onToneDelayChanged(DELAY_STEP_MILLIS) }
            )
            SettingStepperRow(
                label = stringResource(R.string.settings_hang_up_delay),
                description = stringResource(R.string.settings_hang_up_delay_description),
                value = stringResource(R.string.settings_milliseconds, toneSettings.hangUpDelayMillis),
                canDecrease = toneSettings.hangUpDelayMillis > MIN_DELAY_MILLIS,
                canIncrease = toneSettings.hangUpDelayMillis < MAX_DELAY_MILLIS,
                onDecrease = { onHangUpDelayChanged(-DELAY_STEP_MILLIS) },
                onIncrease = { onHangUpDelayChanged(DELAY_STEP_MILLIS) }
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = DIVIDER_ALPHA))
            TextButton(onClick = onResetClicked, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text(text = stringResource(R.string.settings_reset))
            }
        }
    }
}

private val HorizontalPadding = 24.dp
private val VerticalPadding = 16.dp
private val SectionSpacing = 8.dp
private const val DIVIDER_ALPHA = 0.3f

@Preview
@Composable
private fun SettingsScreenContentPreview() {
    OpenCallTrackerTheme {
        SettingsScreenContent(
            toneSettings = ToneSettings(),
            onToneDelayChanged = {},
            onToneDurationChanged = {},
            onToneRepeatChanged = {},
            onHangUpDelayChanged = {},
            onResetClicked = {},
            onBackClick = {}
        )
    }
}
