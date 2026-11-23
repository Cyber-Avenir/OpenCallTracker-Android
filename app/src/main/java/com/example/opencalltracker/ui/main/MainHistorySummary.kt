package com.example.opencalltracker.ui.main

import com.example.opencalltracker.domain.model.HandledCall

data class MainHistorySummary(val handledToday: Int, val handledTotal: Int, val lastHandledCall: HandledCall?)
