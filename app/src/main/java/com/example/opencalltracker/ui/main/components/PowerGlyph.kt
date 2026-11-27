package com.example.opencalltracker.ui.main.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun PowerGlyph(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val strokeWidth = size.minDimension * STROKE_RATIO
        val inset = strokeWidth / 2f

        drawArc(
            color = color,
            startAngle = ARC_START_ANGLE,
            sweepAngle = ARC_SWEEP_ANGLE,
            useCenter = false,
            topLeft = Offset(inset, inset),
            size = Size(size.width - strokeWidth, size.height - strokeWidth),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
        drawLine(
            color = color,
            start = Offset(size.width / 2f, inset),
            end = Offset(size.width / 2f, size.height * STEM_END_RATIO),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

private const val STROKE_RATIO = 0.1f
private const val ARC_START_ANGLE = -60f
private const val ARC_SWEEP_ANGLE = 300f
private const val STEM_END_RATIO = 0.36f
