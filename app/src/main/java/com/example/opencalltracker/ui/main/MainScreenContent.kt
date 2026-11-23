package com.example.opencalltracker.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.opencalltracker.R
import com.example.opencalltracker.ui.main.components.ActiveDuration
import com.example.opencalltracker.ui.main.components.DetectionStatus
import com.example.opencalltracker.ui.main.components.DetectionStatusChip
import com.example.opencalltracker.ui.main.components.LastHandledCall
import com.example.opencalltracker.ui.main.components.MainTopBar
import com.example.opencalltracker.ui.main.components.NoticeCard
import com.example.opencalltracker.ui.main.components.PowerButton
import com.example.opencalltracker.ui.main.components.StatsRow
import com.example.opencalltracker.ui.theme.OpenCallTrackerTheme

@Composable
fun MainScreenContent(
    uiState: MainUiState,
    onDetectionToggled: () -> Unit,
    onHistoryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier.fillMaxSize(), topBar = { MainTopBar(onHistoryClick = onHistoryClick) }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = HorizontalPadding, vertical = VerticalPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(SectionSpacing)
        ) {
            DetectionStatusChip(isEnabled = uiState.isDetectionEnabled)
            PowerButton(isEnabled = uiState.isDetectionEnabled, onClick = onDetectionToggled)
            DetectionStatus(isEnabled = uiState.isDetectionEnabled)
            if (uiState.isDetectionEnabled) {
                ActiveDuration(seconds = uiState.activeDurationSeconds)
            }
            uiState.lastHandledCall?.let { lastHandledCall ->
                LastHandledCall(handledCall = lastHandledCall)
            }
            StatsRow(
                handledCallsToday = uiState.handledCallsToday,
                handledCallsTotal = uiState.handledCallsTotal
            )
            if (!uiState.isDefaultDialer) {
                NoticeCard(text = stringResource(R.string.main_notice_default_dialer), iconRes = R.drawable.ic_warning)
            }
            NoticeCard(text = stringResource(R.string.main_notice_background), iconRes = R.drawable.ic_info)
        }
    }
}

private val HorizontalPadding = 24.dp
private val VerticalPadding = 24.dp
private val SectionSpacing = 20.dp

@Preview(name = "Detection off", heightDp = 900)
@Composable
private fun MainScreenContentInactivePreview() {
    OpenCallTrackerTheme {
        MainScreenContent(uiState = MainUiState(), onDetectionToggled = {}, onHistoryClick = {})
    }
}

@Preview(name = "Detection active", heightDp = 900)
@Composable
private fun MainScreenContentActivePreview() {
    OpenCallTrackerTheme(darkTheme = true) {
        MainScreenContent(
            uiState = MainUiState(
                isDetectionEnabled = true,
                activeDurationSeconds = 3_671L,
                handledCallsToday = 4,
                handledCallsTotal = 128,
                isDefaultDialer = true
            ),
            onDetectionToggled = {},
            onHistoryClick = {}
        )
    }
}
