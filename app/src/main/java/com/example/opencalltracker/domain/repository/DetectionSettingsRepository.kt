package com.example.opencalltracker.domain.repository

import com.example.opencalltracker.domain.model.DetectionState
import kotlinx.coroutines.flow.Flow

interface DetectionSettingsRepository {
    fun observeDetectionState(): Flow<DetectionState>

    suspend fun setDetectionEnabled(isEnabled: Boolean)
}
