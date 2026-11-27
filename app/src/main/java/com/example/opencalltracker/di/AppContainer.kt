package com.example.opencalltracker.di

import android.content.Context
import androidx.room.Room
import com.example.opencalltracker.data.OpenCallTrackerDatabase
import com.example.opencalltracker.data.history.RoomCallHistoryRepository
import com.example.opencalltracker.data.role.RoleManagerDialerRoleRepository
import com.example.opencalltracker.data.settings.DataStoreDetectionSettingsRepository
import com.example.opencalltracker.data.settings.DataStoreToneSettingsRepository
import com.example.opencalltracker.domain.repository.CallHistoryRepository
import com.example.opencalltracker.domain.repository.DetectionSettingsRepository
import com.example.opencalltracker.domain.repository.DialerRoleRepository
import com.example.opencalltracker.domain.repository.ToneSettingsRepository

class AppContainer(context: Context) {
    private val database: OpenCallTrackerDatabase =
        Room.databaseBuilder(context, OpenCallTrackerDatabase::class.java, DATABASE_NAME).build()

    val detectionSettingsRepository: DetectionSettingsRepository = DataStoreDetectionSettingsRepository(context)

    val callHistoryRepository: CallHistoryRepository = RoomCallHistoryRepository(database.handledCallDao())

    val dialerRoleRepository: DialerRoleRepository = RoleManagerDialerRoleRepository(context)

    val toneSettingsRepository: ToneSettingsRepository = DataStoreToneSettingsRepository(context)
}

private const val DATABASE_NAME = "open_call_tracker.db"
