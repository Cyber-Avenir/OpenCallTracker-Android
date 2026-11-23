package com.example.opencalltracker.ui.history

import com.example.opencalltracker.domain.model.HandledCall

data class HistoryUiState(val handledCalls: List<HandledCall> = emptyList(), val isLoading: Boolean = true)
