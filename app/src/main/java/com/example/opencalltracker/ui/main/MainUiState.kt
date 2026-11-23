package com.example.opencalltracker.ui.main

data class MainUiState(
    val isDetectionEnabled: Boolean = false,
    val activeDurationSeconds: Long = 0L,
    val handledCallsToday: Int = 0,
    val handledCallsTotal: Int = 0,
    val isDefaultDialer: Boolean = false
)
