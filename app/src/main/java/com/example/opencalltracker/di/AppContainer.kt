package com.example.opencalltracker.di

import android.content.Context
import androidx.room.Room
import com.example.opencalltracker.data.OpenCallTrackerDatabase
import com.example.opencalltracker.data.history.RoomCallHistoryRepository
import com.example.opencalltracker.data.settings.DataStoreDetectionSettingsRepository
import com.example.opencalltracker.domain.repository.CallHistoryRepository
import com.example.opencalltracker.domain.repository.DetectionSettingsRepository

class AppContainer(context: Context) {
    private val database: OpenCallTrackerDatabase =
        Room.databaseBuilder(context, OpenCallTrackerDatabase::class.java, DATABASE_NAME).build()

    val detectionSettingsRepository: DetectionSettingsRepository = DataStoreDetectionSettingsRepository(context)

    val callHistoryRepository: CallHistoryRepository = RoomCallHistoryRepository(database.handledCallDao())
}

private const val DATABASE_NAME = "open_call_tracker.db"
