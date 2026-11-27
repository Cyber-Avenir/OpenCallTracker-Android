package com.example.opencalltracker.data.history

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "handled_calls")
data class HandledCallEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = "phone_number") val phoneNumber: String?,
    @ColumnInfo(name = "handled_at") val handledAt: Long,
    @ColumnInfo(name = "outcome") val outcome: String
)
