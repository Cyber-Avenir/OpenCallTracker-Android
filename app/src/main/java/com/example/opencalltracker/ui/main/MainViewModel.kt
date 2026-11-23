package com.example.opencalltracker.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val mutableUiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = mutableUiState.asStateFlow()

    private var durationJob: Job? = null

    fun onDetectionToggled() {
        val isEnabled = !mutableUiState.value.isDetectionEnabled
        mutableUiState.update { it.copy(isDetectionEnabled = isEnabled, activeDurationSeconds = 0L) }

        durationJob?.cancel()
        durationJob = if (isEnabled) startDurationTicker() else null
    }

    private fun startDurationTicker(): Job = viewModelScope.launch {
        while (true) {
            delay(DURATION_TICK_MILLIS)
            mutableUiState.update { it.copy(activeDurationSeconds = it.activeDurationSeconds + 1) }
        }
    }
}

private const val DURATION_TICK_MILLIS = 1_000L
