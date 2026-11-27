package com.example.opencalltracker.domain.model

data class ToneSettings(
    val toneDelayMillis: Long = DEFAULT_TONE_DELAY_MILLIS,
    val toneDurationMillis: Long = DEFAULT_TONE_DURATION_MILLIS,
    val toneRepeatCount: Int = DEFAULT_TONE_REPEAT_COUNT,
    val hangUpDelayMillis: Long = DEFAULT_HANG_UP_DELAY_MILLIS
)

const val DEFAULT_TONE_DELAY_MILLIS = 3_000L
const val DEFAULT_TONE_DURATION_MILLIS = 1_200L
const val DEFAULT_TONE_REPEAT_COUNT = 2
const val DEFAULT_HANG_UP_DELAY_MILLIS = 2_000L
