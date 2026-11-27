package com.example.opencalltracker.ui.main.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.opencalltracker.R
import com.example.opencalltracker.ui.theme.OpenCallTrackerTheme

@Composable
fun PowerButton(isEnabled: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val accentColor = MaterialTheme.colorScheme.primary

    val containerColor by animateColorAsState(
        targetValue = if (isEnabled) accentColor else MaterialTheme.colorScheme.surfaceVariant,
        label = "containerColor"
    )
    val glyphColor by animateColorAsState(
        targetValue = if (isEnabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
        label = "glyphColor"
    )
    val glowElevation by animateDpAsState(targetValue = if (isEnabled) ActiveGlowElevation else 0.dp, label = "glowElevation")
    val pressScale by animateFloatAsState(targetValue = if (isPressed) PRESSED_SCALE else 1f, label = "pressScale")

    Box(modifier = modifier.size(PulseDiameter), contentAlignment = Alignment.Center) {
        if (isEnabled) {
            PowerButtonPulse(color = accentColor, modifier = Modifier.fillMaxSize())
        }
        Box(
            modifier = Modifier
                .size(ButtonDiameter)
                .scale(pressScale)
                .shadow(elevation = glowElevation, shape = CircleShape, ambientColor = accentColor, spotColor = accentColor)
                .clip(CircleShape)
                .background(containerColor)
                .clickable(
                    interactionSource = interactionSource,
                    indication = LocalIndication.current,
                    onClickLabel = stringResource(if (isEnabled) R.string.main_power_button_turn_off else R.string.main_power_button_turn_on),
                    role = Role.Button,
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center
        ) {
            PowerGlyph(color = glyphColor, modifier = Modifier.size(GlyphSize))
        }
    }
}

private val PulseDiameter = 280.dp
private val ButtonDiameter = 200.dp
private val GlyphSize = 76.dp
private val ActiveGlowElevation = 28.dp
private const val PRESSED_SCALE = 0.95f

@Preview(name = "Off")
@Composable
private fun PowerButtonInactivePreview() {
    OpenCallTrackerTheme {
        PowerButton(isEnabled = false, onClick = {})
    }
}

@Preview(name = "On")
@Composable
private fun PowerButtonActivePreview() {
    OpenCallTrackerTheme(darkTheme = true) {
        PowerButton(isEnabled = true, onClick = {})
    }
}
