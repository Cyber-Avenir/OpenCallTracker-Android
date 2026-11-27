package com.example.opencalltracker.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.opencalltracker.domain.model.DEFAULT_HANG_UP_DELAY_MILLIS
import com.example.opencalltracker.domain.model.DEFAULT_TONE_DELAY_MILLIS
import com.example.opencalltracker.domain.model.DEFAULT_TONE_DURATION_MILLIS
import com.example.opencalltracker.domain.model.DEFAULT_TONE_REPEAT_COUNT
import com.example.opencalltracker.domain.model.ToneSettings
import com.example.opencalltracker.domain.repository.ToneSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.toneSettingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "tone_settings")

class DataStoreToneSettingsRepository(private val context: Context) : ToneSettingsRepository {
    override fun observeToneSettings(): Flow<ToneSettings> = context.toneSettingsDataStore.data
        .catch { emit(emptyPreferences()) }
        .map { preferences ->
            ToneSettings(
                toneDelayMillis = preferences[ToneDelayKey] ?: DEFAULT_TONE_DELAY_MILLIS,
                toneDurationMillis = preferences[ToneDurationKey] ?: DEFAULT_TONE_DURATION_MILLIS,
                toneRepeatCount = preferences[ToneRepeatCountKey] ?: DEFAULT_TONE_REPEAT_COUNT,
                hangUpDelayMillis = preferences[HangUpDelayKey] ?: DEFAULT_HANG_UP_DELAY_MILLIS
            )
        }

    override suspend fun setToneSettings(toneSettings: ToneSettings) {
        context.toneSettingsDataStore.edit { preferences ->
            preferences[ToneDelayKey] = toneSettings.toneDelayMillis
            preferences[ToneDurationKey] = toneSettings.toneDurationMillis
            preferences[ToneRepeatCountKey] = toneSettings.toneRepeatCount
            preferences[HangUpDelayKey] = toneSettings.hangUpDelayMillis
        }
    }
}

private val ToneDelayKey = longPreferencesKey("tone_delay_millis")
private val ToneDurationKey = longPreferencesKey("tone_duration_millis")
private val ToneRepeatCountKey = intPreferencesKey("tone_repeat_count")
private val HangUpDelayKey = longPreferencesKey("hang_up_delay_millis")
