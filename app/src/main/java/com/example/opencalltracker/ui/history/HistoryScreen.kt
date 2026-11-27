package com.example.opencalltracker.ui.history

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HistoryScreen(onBackClick: () -> Unit, modifier: Modifier = Modifier, viewModel: HistoryViewModel = viewModel(factory = HistoryViewModel.Factory)) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HistoryScreenContent(uiState = uiState, onBackClick = onBackClick, modifier = modifier)
}
