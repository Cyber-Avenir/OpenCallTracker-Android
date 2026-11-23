package com.example.opencalltracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Emerald80,
    onPrimary = Emerald20,
    primaryContainer = Emerald30,
    onPrimaryContainer = Emerald90,
    secondary = Sage80,
    background = Slate10,
    onBackground = Slate95,
    surface = Slate10,
    onSurface = Slate95,
    surfaceVariant = Slate30,
    onSurfaceVariant = Slate80,
    outline = Slate70,
    error = Red80
)

private val LightColorScheme = lightColorScheme(
    primary = Emerald40,
    onPrimary = Color.White,
    primaryContainer = Emerald90,
    onPrimaryContainer = EmeraldInk,
    secondary = Sage40,
    background = Slate99,
    onBackground = Slate20,
    surface = Slate99,
    onSurface = Slate20,
    surfaceVariant = Slate90,
    onSurfaceVariant = Slate30,
    outline = Slate60,
    error = Red40
)

@Composable
fun OpenCallTrackerTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
