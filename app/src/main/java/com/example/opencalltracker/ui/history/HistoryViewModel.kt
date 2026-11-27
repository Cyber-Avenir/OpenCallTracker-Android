package com.example.opencalltracker.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.opencalltracker.OpenCallTrackerApplication
import com.example.opencalltracker.domain.repository.CallHistoryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HistoryViewModel(callHistoryRepository: CallHistoryRepository) : ViewModel() {
    val uiState: StateFlow<HistoryUiState> = callHistoryRepository.observeHandledCalls()
        .map { handledCalls -> HistoryUiState(handledCalls = handledCalls, isLoading = false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STATE_TIMEOUT_MILLIS),
            initialValue = HistoryUiState()
        )

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val container = (this[APPLICATION_KEY] as OpenCallTrackerApplication).container
                HistoryViewModel(container.callHistoryRepository)
            }
        }
    }
}

private const val STATE_TIMEOUT_MILLIS = 5_000L
