package com.example.opencalltracker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.opencalltracker.data.history.HandledCallDao
import com.example.opencalltracker.data.history.HandledCallEntity

@Database(entities = [HandledCallEntity::class], version = 1, exportSchema = false)
abstract class OpenCallTrackerDatabase : RoomDatabase() {
    abstract fun handledCallDao(): HandledCallDao
}
