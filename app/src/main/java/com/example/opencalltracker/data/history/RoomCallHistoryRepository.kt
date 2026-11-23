package com.example.opencalltracker.data.history

import com.example.opencalltracker.domain.model.HandledCall
import com.example.opencalltracker.domain.model.HandledCallOutcome
import com.example.opencalltracker.domain.repository.CallHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomCallHistoryRepository(private val handledCallDao: HandledCallDao) : CallHistoryRepository {
    override fun observeHandledCalls(): Flow<List<HandledCall>> = handledCallDao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override fun observeLastHandledCall(): Flow<HandledCall?> = handledCallDao.observeLast().map { entity -> entity?.toDomain() }

    override fun observeHandledCallCount(): Flow<Int> = handledCallDao.observeCount()

    override fun observeHandledCallCountSince(sinceMillis: Long): Flow<Int> = handledCallDao.observeCountSince(sinceMillis)

    override suspend fun recordHandledCall(phoneNumber: String?, handledAt: Long, outcome: HandledCallOutcome) {
        handledCallDao.insert(
            HandledCallEntity(phoneNumber = phoneNumber, handledAt = handledAt, outcome = outcome.name)
        )
    }
}
