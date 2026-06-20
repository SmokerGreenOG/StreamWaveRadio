package com.streamwave.radio.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val StreamWaveColors = darkColorScheme(
    primary = Purple, onPrimary = PrimaryText,
    primaryContainer = DarkPurple, onPrimaryContainer = LightPurple,
    secondary = Pink, onSecondary = PrimaryText,
    secondaryContainer = Card, onSecondaryContainer = LightPink,
    tertiary = Cyan, onTertiary = PrimaryText,
    background = Background, onBackground = PrimaryText,
    surface = Panel, onSurface = PrimaryText,
    surfaceVariant = Card, onSurfaceVariant = SecondaryText,
    error = ErrorRed, onError = PrimaryText,
    outline = NeonBorder, outlineVariant = GlassBorder
)

@Composable
fun StreamWaveTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = StreamWaveColors,
        typography = AppTypography,
        content = content
    )
}
