package com.example.opencalltracker.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.opencalltracker.domain.model.DetectionState
import com.example.opencalltracker.domain.repository.DetectionSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.detectionSettingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "detection_settings")

class DataStoreDetectionSettingsRepository(private val context: Context) : DetectionSettingsRepository {
    override fun observeDetectionState(): Flow<DetectionState> = context.detectionSettingsDataStore.data
        .catch { emit(emptyPreferences()) }
        .map { preferences ->
            DetectionState(
                isEnabled = preferences[IsDetectionEnabledKey] ?: false,
                enabledAt = preferences[DetectionEnabledAtKey]
            )
        }

    override suspend fun setDetectionEnabled(isEnabled: Boolean) {
        context.detectionSettingsDataStore.edit { preferences ->
            preferences[IsDetectionEnabledKey] = isEnabled
            if (isEnabled) {
                preferences[DetectionEnabledAtKey] = System.currentTimeMillis()
            } else {
                preferences.remove(DetectionEnabledAtKey)
            }
        }
    }
}

private val IsDetectionEnabledKey = booleanPreferencesKey("is_detection_enabled")
private val DetectionEnabledAtKey = longPreferencesKey("detection_enabled_at")
