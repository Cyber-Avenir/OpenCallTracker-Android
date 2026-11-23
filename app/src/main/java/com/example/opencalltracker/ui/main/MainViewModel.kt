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
import com.example.opencalltracker.domain.repository.DialerRoleRepository
import java.time.LocalDate
import java.time.ZoneId
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val detectionSettingsRepository: DetectionSettingsRepository,
    private val dialerRoleRepository: DialerRoleRepository,
    callHistoryRepository: CallHistoryRepository
) : ViewModel() {
    private val isDefaultDialerState = MutableStateFlow(dialerRoleRepository.isDefaultDialer())

    private val secondTicks: Flow<Unit> = flow {
        while (true) {
            emit(Unit)
            delay(DURATION_TICK_MILLIS)
        }
    }

    private val historySummary: Flow<MainHistorySummary> = combine(
        callHistoryRepository.observeHandledCallCountSince(startOfTodayMillis()),
        callHistoryRepository.observeHandledCallCount(),
        callHistoryRepository.observeLastHandledCall()
    ) { handledToday, handledTotal, lastHandledCall ->
        MainHistorySummary(handledToday = handledToday, handledTotal = handledTotal, lastHandledCall = lastHandledCall)
    }

    val uiState: StateFlow<MainUiState> = combine(
        detectionSettingsRepository.observeDetectionState(),
        historySummary,
        isDefaultDialerState,
        secondTicks
    ) { detectionState, history, isDefaultDialer, _ ->
        MainUiState(
            isDetectionEnabled = detectionState.isEnabled,
            activeDurationSeconds = elapsedSecondsSince(detectionState.enabledAt),
            handledCallsToday = history.handledToday,
            handledCallsTotal = history.handledTotal,
            lastHandledCall = history.lastHandledCall,
            isDefaultDialer = isDefaultDialer
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

    fun refreshDialerRole() {
        isDefaultDialerState.value = dialerRoleRepository.isDefaultDialer()
    }

    // The role request is triggered by the power button: detection starts only once it is granted.
    fun onDialerRoleRequestResult() {
        refreshDialerRole()
        if (isDefaultDialerState.value && !uiState.value.isDetectionEnabled) {
            onDetectionToggled()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val container = (this[APPLICATION_KEY] as OpenCallTrackerApplication).container
                MainViewModel(
                    detectionSettingsRepository = container.detectionSettingsRepository,
                    dialerRoleRepository = container.dialerRoleRepository,
                    callHistoryRepository = container.callHistoryRepository
                )
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
