package com.example.opencalltracker.ui.main

import com.example.opencalltracker.domain.model.HandledCall

data class MainUiState(
    val isDetectionEnabled: Boolean = false,
    val activeDurationSeconds: Long = 0L,
    val handledCallsToday: Int = 0,
    val handledCallsTotal: Int = 0,
    val lastHandledCall: HandledCall? = null,
    val isDefaultDialer: Boolean = false
)
