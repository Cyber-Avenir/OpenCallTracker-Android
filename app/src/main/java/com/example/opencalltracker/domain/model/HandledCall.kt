package com.example.opencalltracker.domain.model

data class HandledCall(
    val id: Long,
    val phoneNumber: String?,
    val handledAt: Long,
    val outcome: HandledCallOutcome
)
