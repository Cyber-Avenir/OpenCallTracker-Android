package com.example.opencalltracker.domain.repository

import com.example.opencalltracker.domain.model.ToneSettings
import kotlinx.coroutines.flow.Flow

interface ToneSettingsRepository {
    fun observeToneSettings(): Flow<ToneSettings>

    suspend fun setToneSettings(toneSettings: ToneSettings)
}
