package com.example.opencalltracker.domain.repository

import com.example.opencalltracker.domain.model.HandledCall
import com.example.opencalltracker.domain.model.HandledCallOutcome
import kotlinx.coroutines.flow.Flow

interface CallHistoryRepository {
    fun observeHandledCalls(): Flow<List<HandledCall>>

    fun observeHandledCallCount(): Flow<Int>

    fun observeHandledCallCountSince(sinceMillis: Long): Flow<Int>

    suspend fun recordHandledCall(phoneNumber: String?, handledAt: Long, outcome: HandledCallOutcome)
}
