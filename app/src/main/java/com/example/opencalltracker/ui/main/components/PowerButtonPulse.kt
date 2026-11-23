package com.example.opencalltracker.ui.main.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun PowerButtonPulse(color: Color, modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "pulse")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(durationMillis = PULSE_DURATION_MILLIS, easing = LinearEasing)),
        label = "pulseProgress"
    )

    Canvas(modifier = modifier) {
        val auraRadius = size.minDimension / 2f
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(color.copy(alpha = AURA_ALPHA), Color.Transparent),
                center = center,
                radius = auraRadius
            ),
            radius = auraRadius
        )

        val strokeWidth = PulseStrokeWidth.toPx()
        val maxRadius = (size.minDimension - strokeWidth) / 2f
        RingDelays.forEach { delay ->
            val ringProgress = (progress + delay) % 1f
            drawCircle(
                color = color,
                radius = maxRadius * (MIN_RADIUS_RATIO + (1f - MIN_RADIUS_RATIO) * ringProgress),
                alpha = (1f - ringProgress) * MAX_RING_ALPHA,
                style = Stroke(width = strokeWidth)
            )
        }
    }
}

private val RingDelays = listOf(0f, 0.5f)
private val PulseStrokeWidth = 2.dp
private const val PULSE_DURATION_MILLIS = 2600
private const val MIN_RADIUS_RATIO = 0.74f
private const val MAX_RING_ALPHA = 0.4f
private const val AURA_ALPHA = 0.18f
