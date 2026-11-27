package com.example.opencalltracker.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.opencalltracker.OpenCallTrackerApplication
import com.example.opencalltracker.domain.model.ToneSettings
import com.example.opencalltracker.domain.repository.ToneSettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val toneSettingsRepository: ToneSettingsRepository) : ViewModel() {
    val toneSettings: StateFlow<ToneSettings> = toneSettingsRepository.observeToneSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STATE_TIMEOUT_MILLIS),
            initialValue = ToneSettings()
        )

    fun onToneDelayChanged(stepMillis: Long) {
        val current = toneSettings.value
        val updated = (current.toneDelayMillis + stepMillis).coerceIn(MIN_DELAY_MILLIS, MAX_DELAY_MILLIS)
        save(current.copy(toneDelayMillis = updated))
    }

    fun onToneDurationChanged(stepMillis: Long) {
        val current = toneSettings.value
        val updated = (current.toneDurationMillis + stepMillis).coerceIn(MIN_DURATION_MILLIS, MAX_DURATION_MILLIS)
        save(current.copy(toneDurationMillis = updated))
    }

    fun onToneRepeatChanged(step: Int) {
        val current = toneSettings.value
        val updated = (current.toneRepeatCount + step).coerceIn(MIN_REPEAT_COUNT, MAX_REPEAT_COUNT)
        save(current.copy(toneRepeatCount = updated))
    }

    fun onHangUpDelayChanged(stepMillis: Long) {
        val current = toneSettings.value
        val updated = (current.hangUpDelayMillis + stepMillis).coerceIn(MIN_DELAY_MILLIS, MAX_DELAY_MILLIS)
        save(current.copy(hangUpDelayMillis = updated))
    }

    fun onResetClicked() {
        save(ToneSettings())
    }

    private fun save(toneSettings: ToneSettings) {
        viewModelScope.launch {
            toneSettingsRepository.setToneSettings(toneSettings)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val container = (this[APPLICATION_KEY] as OpenCallTrackerApplication).container
                SettingsViewModel(container.toneSettingsRepository)
            }
        }
    }
}

const val DELAY_STEP_MILLIS = 500L
const val DURATION_STEP_MILLIS = 100L
const val REPEAT_STEP = 1
const val MIN_DELAY_MILLIS = 0L
const val MAX_DELAY_MILLIS = 15_000L
const val MIN_DURATION_MILLIS = 100L
const val MAX_DURATION_MILLIS = 5_000L
const val MIN_REPEAT_COUNT = 1
const val MAX_REPEAT_COUNT = 5

private const val STATE_TIMEOUT_MILLIS = 5_000L
