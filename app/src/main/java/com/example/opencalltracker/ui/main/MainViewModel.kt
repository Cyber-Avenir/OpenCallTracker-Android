package com.example.opencalltracker.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.opencalltracker.OpenCallTrackerApplication
import com.example.opencalltracker.domain.repository.CallHistoryRepository
import com.example.opencalltracker.domain.repository.DetectionSettingsRepository
import java.time.LocalDate
import java.time.ZoneId
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val detectionSettingsRepository: DetectionSettingsRepository,
    callHistoryRepository: CallHistoryRepository
) : ViewModel() {
    private val secondTicks: Flow<Unit> = flow {
        while (true) {
            emit(Unit)
            delay(DURATION_TICK_MILLIS)
        }
    }

    val uiState: StateFlow<MainUiState> = combine(
        detectionSettingsRepository.observeDetectionState(),
        callHistoryRepository.observeHandledCallCountSince(startOfTodayMillis()),
        callHistoryRepository.observeHandledCallCount(),
        secondTicks
    ) { detectionState, handledCallsToday, handledCallsTotal, _ ->
        MainUiState(
            isDetectionEnabled = detectionState.isEnabled,
            activeDurationSeconds = elapsedSecondsSince(detectionState.enabledAt),
            handledCallsToday = handledCallsToday,
            handledCallsTotal = handledCallsTotal
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STATE_TIMEOUT_MILLIS),
        initialValue = MainUiState()
    )

    fun onDetectionToggled() {
        viewModelScope.launch {
            detectionSettingsRepository.setDetectionEnabled(!uiState.value.isDetectionEnabled)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val container = (this[APPLICATION_KEY] as OpenCallTrackerApplication).container
                MainViewModel(container.detectionSettingsRepository, container.callHistoryRepository)
            }
        }
    }
}

private fun elapsedSecondsSince(startMillis: Long?): Long {
    if (startMillis == null) {
        return 0L
    }
    return ((System.currentTimeMillis() - startMillis) / MILLIS_PER_SECOND).coerceAtLeast(0L)
}

private fun startOfTodayMillis(): Long = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

private const val DURATION_TICK_MILLIS = 1_000L
private const val MILLIS_PER_SECOND = 1_000L
private const val STATE_TIMEOUT_MILLIS = 5_000L
