package com.example.opencalltracker.service

import android.content.Intent
import android.os.Build
import android.os.IBinder
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
import kotlinx.coroutines.runBlocking

class CallInterceptorService : InCallService() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val callbacks = mutableMapOf<Call, Call.Callback>()
    private var interceptedCall: Call? = null

    private val container: AppContainer
        get() = (application as OpenCallTrackerApplication).container

    // Telecom falls back to the preloaded dialer when this returns null, which hands every call
    // back to the stock phone app while detection is off.
    override fun onBind(intent: Intent?): IBinder? {
        if (!runBlocking { isDetectionEnabled() }) {
            return null
        }
        return super.onBind(intent)
    }

    override fun onCallAdded(call: Call) {
        super.onCallAdded(call)
        serviceScope.launch {
            val canIntercept = interceptedCall == null &&
                call.currentState == Call.STATE_RINGING &&
                isDetectionEnabled()
            if (canIntercept) {
                intercept(call)
            }
        }
    }

    override fun onCallRemoved(call: Call) {
        super.onCallRemoved(call)
        unregister(call)
        if (interceptedCall == call) {
            interceptedCall = null
        }
    }

    override fun onDestroy() {
        serviceScope.cancel()
        callbacks.keys.toList().forEach { unregister(it) }
        interceptedCall = null
        super.onDestroy()
    }

    private fun intercept(call: Call) {
        interceptedCall = call
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
        val toneSettings = container.toneSettingsRepository.observeToneSettings().first()

        val outcome = runCatching {
            delay(toneSettings.toneDelayMillis)
            // The caller may have hung up while we were waiting.
            check(call.currentState == Call.STATE_ACTIVE)
            repeat(toneSettings.toneRepeatCount) { index ->
                if (index > 0) {
                    delay(TONE_GAP_MILLIS)
                }
                call.playDtmfTone(DTMF_DIGIT)
                delay(toneSettings.toneDurationMillis)
                call.stopDtmfTone()
            }
            delay(toneSettings.hangUpDelayMillis)
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

        if (interceptedCall == call) {
            interceptedCall = null
        }
    }

    private suspend fun isDetectionEnabled(): Boolean = container.detectionSettingsRepository.observeDetectionState().first().isEnabled

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

private const val DTMF_DIGIT = '*'
private const val TONE_GAP_MILLIS = 500L
