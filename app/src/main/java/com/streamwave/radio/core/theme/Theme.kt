package com.streamwave.radio.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val StreamWaveColorScheme = darkColorScheme(
    primary = Purple,
    onPrimary = PrimaryText,
    primaryContainer = DarkPurple,
    onPrimaryContainer = LightPurple,
    secondary = Pink,
    onSecondary = PrimaryText,
    secondaryContainer = Card,
    onSecondaryContainer = LightPink,
    tertiary = Cyan,
    onTertiary = PrimaryText,
    tertiaryContainer = Blue,
    onTertiaryContainer = LightBlue,
    background = Background,
    onBackground = PrimaryText,
    surface = Panel,
    onSurface = PrimaryText,
    surfaceVariant = Card,
    onSurfaceVariant = SecondaryText,
    error = ErrorRed,
    onError = PrimaryText,
    outline = GlassBorder,
    outlineVariant = GlassOverlay
)

@Composable
fun StreamWaveTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = StreamWaveColorScheme,
        typography = AppTypography,
        content = content
    )
}
