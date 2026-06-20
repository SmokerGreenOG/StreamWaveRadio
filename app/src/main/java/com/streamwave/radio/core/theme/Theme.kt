package com.streamwave.radio.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun StreamWaveTheme(content: @Composable () -> Unit) {
    val t = currentTheme
    val scheme = darkColorScheme(
        primary = t.primary, onPrimary = PrimaryText,
        primaryContainer = t.primaryDark, onPrimaryContainer = t.primaryLight,
        secondary = t.accent, onSecondary = PrimaryText,
        secondaryContainer = Card, onSecondaryContainer = PrimaryText,
        tertiary = Cyan, onTertiary = PrimaryText,
        background = Background, onBackground = PrimaryText,
        surface = Panel, onSurface = PrimaryText,
        surfaceVariant = Card, onSurfaceVariant = SecondaryText,
        error = ErrorRed, onError = PrimaryText,
        outline = t.primaryGlow, outlineVariant = GlassBorder
    )
    CompositionLocalProvider(LocalStreamWaveColors provides t) {
        MaterialTheme(colorScheme = scheme, typography = AppTypography, content = content)
    }
}

fun applyTheme(themeIndex: Int) {
    currentTheme = AllThemes.getOrElse(themeIndex) { PurpleTheme }
}
