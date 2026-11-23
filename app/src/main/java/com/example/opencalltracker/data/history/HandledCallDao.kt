package com.example.opencalltracker.data.history

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HandledCallDao {
    @Query("SELECT * FROM handled_calls ORDER BY handled_at DESC")
    fun observeAll(): Flow<List<HandledCallEntity>>

    @Query("SELECT COUNT(*) FROM handled_calls")
    fun observeCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM handled_calls WHERE handled_at >= :sinceMillis")
    fun observeCountSince(sinceMillis: Long): Flow<Int>

    @Insert
    suspend fun insert(handledCall: HandledCallEntity)
}
