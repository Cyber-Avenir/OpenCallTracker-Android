package com.example.opencalltracker.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SettingsScreen(onBackClick: () -> Unit, modifier: Modifier = Modifier, viewModel: SettingsViewModel = viewModel(factory = SettingsViewModel.Factory)) {
    val toneSettings by viewModel.toneSettings.collectAsStateWithLifecycle()

    SettingsScreenContent(
        toneSettings = toneSettings,
        onToneDelayChanged = viewModel::onToneDelayChanged,
        onToneDurationChanged = viewModel::onToneDurationChanged,
        onToneRepeatChanged = viewModel::onToneRepeatChanged,
        onHangUpDelayChanged = viewModel::onHangUpDelayChanged,
        onResetClicked = viewModel::onResetClicked,
        onBackClick = onBackClick,
        modifier = modifier
    )
}
