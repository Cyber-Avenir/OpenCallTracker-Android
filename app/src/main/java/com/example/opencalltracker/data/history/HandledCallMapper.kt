package com.example.opencalltracker.data.history

import com.example.opencalltracker.domain.model.HandledCall
import com.example.opencalltracker.domain.model.HandledCallOutcome

fun HandledCallEntity.toDomain(): HandledCall = HandledCall(
    id = id,
    phoneNumber = phoneNumber,
    handledAt = handledAt,
    outcome = HandledCallOutcome.entries.firstOrNull { it.name == outcome } ?: HandledCallOutcome.FAILED
)

fun HandledCall.toEntity(): HandledCallEntity = HandledCallEntity(
    id = id,
    phoneNumber = phoneNumber,
    handledAt = handledAt,
    outcome = outcome.name
)
