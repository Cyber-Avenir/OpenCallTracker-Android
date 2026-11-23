package com.example.opencalltracker.service

import android.os.Build
import android.telecom.Call
import android.telecom.InCallService
import android.telecom.VideoProfile
import com.example.opencalltracker.OpenCallTrackerApplication
import com.example.opencalltracker.di.AppContainer
import com.example.opencalltracker.domain.model.HandledCallOutcome
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CallInterceptorService : InCallService() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val callbacks = mutableMapOf<Call, Call.Callback>()

    private val container: AppContainer
        get() = (application as OpenCallTrackerApplication).container

    override fun onCallAdded(call: Call) {
        super.onCallAdded(call)
        serviceScope.launch {
            val isDetectionEnabled = container.detectionSettingsRepository.observeDetectionState().first().isEnabled
            if (isDetectionEnabled && call.currentState == Call.STATE_RINGING) {
                intercept(call)
            }
        }
    }

    override fun onCallRemoved(call: Call) {
        super.onCallRemoved(call)
        unregister(call)
    }

    override fun onDestroy() {
        serviceScope.cancel()
        callbacks.keys.toList().forEach { unregister(it) }
        super.onDestroy()
    }

    private fun intercept(call: Call) {
        val phoneNumber = call.details?.handle?.schemeSpecificPart

        val callback = object : Call.Callback() {
            override fun onStateChanged(changedCall: Call, state: Int) {
                if (state == Call.STATE_ACTIVE) {
                    unregister(changedCall)
                    serviceScope.launch { sendToneAndHangUp(changedCall, phoneNumber) }
                }
            }
        }

        callbacks[call] = callback
        call.registerCallback(callback)
        call.answer(VideoProfile.STATE_AUDIO_ONLY)
    }

    private suspend fun sendToneAndHangUp(call: Call, phoneNumber: String?) {
        val outcome = runCatching {
            call.playDtmfTone(DTMF_DIGIT)
            delay(TONE_DURATION_MILLIS)
            call.stopDtmfTone()
            delay(HANG_UP_DELAY_MILLIS)
            call.disconnect()
        }.fold(
            onSuccess = { HandledCallOutcome.COMPLETED },
            onFailure = { HandledCallOutcome.FAILED }
        )

        container.callHistoryRepository.recordHandledCall(
            phoneNumber = phoneNumber,
            handledAt = System.currentTimeMillis(),
            outcome = outcome
        )
    }

    private fun unregister(call: Call) {
        callbacks.remove(call)?.let { call.unregisterCallback(it) }
    }
}

private val Call.currentState: Int
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        details.state
    } else {
        @Suppress("DEPRECATION")
        state
    }

private const val DTMF_DIGIT = '#'
private const val TONE_DURATION_MILLIS = 350L
private const val HANG_UP_DELAY_MILLIS = 600L
