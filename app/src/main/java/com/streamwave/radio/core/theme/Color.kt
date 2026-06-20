package com.streamwave.radio.core.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

// === Thema kleuren ===
data class StreamWaveColors(
    val primary: Color, val primaryGlow: Color, val primaryLight: Color, val primaryDark: Color,
    val accent: Color, val accentGlow: Color,
    val name: String
)

val PurpleTheme = StreamWaveColors(
    primary = Color(0xFFA855F7), primaryGlow = Color(0x40A855F7), primaryLight = Color(0xFFC084FC), primaryDark = Color(0xFF7C3AED),
    accent = Color(0xFFEC4899), accentGlow = Color(0x40EC4899),
    name = "Paars"
)
val BlueTheme = StreamWaveColors(
    primary = Color(0xFF3B82F6), primaryGlow = Color(0x403B82F6), primaryLight = Color(0xFF60A5FA), primaryDark = Color(0xFF2563EB),
    accent = Color(0xFF06B6D4), accentGlow = Color(0x4006B6D4),
    name = "Blauw"
)
val GreenTheme = StreamWaveColors(
    primary = Color(0xFF22C55E), primaryGlow = Color(0x4022C55E), primaryLight = Color(0xFF4ADE80), primaryDark = Color(0xFF16A34A),
    accent = Color(0xFF06B6D4), accentGlow = Color(0x4006B6D4),
    name = "Groen"
)
val RedTheme = StreamWaveColors(
    primary = Color(0xFFEF4444), primaryGlow = Color(0x40EF4444), primaryLight = Color(0xFFF87171), primaryDark = Color(0xFFDC2626),
    accent = Color(0xFFF97316), accentGlow = Color(0x40F97316),
    name = "Rood"
)
val OrangeTheme = StreamWaveColors(
    primary = Color(0xFFF97316), primaryGlow = Color(0x40F97316), primaryLight = Color(0xFFFB923C), primaryDark = Color(0xFFEA580C),
    accent = Color(0xFFEAB308), accentGlow = Color(0x40EAB308),
    name = "Oranje"
)

val AllThemes = listOf(PurpleTheme, BlueTheme, GreenTheme, RedTheme, OrangeTheme)

// === Hoofdkleuren (dynamisch via currentTheme) ===
var currentTheme = PurpleTheme

val Purple get() = currentTheme.primary
val PurpleGlow get() = currentTheme.primaryGlow
val LightPurple get() = currentTheme.primaryLight
val DarkPurple get() = currentTheme.primaryDark
val Pink get() = currentTheme.accent
val PinkGlow get() = currentTheme.accentGlow
val LightPink get() = currentTheme.accent
val Blue get() = Color(0xFF3B82F6)
val Cyan get() = Color(0xFF06B6D4)

val Background = Color(0xFF0D0A1A)
val Panel = Color(0xFF15102A)
val Card = Color(0xFF1C1440)
val ElevatedCard = Color(0xFF251B55)
val GlassBorder = Color(0x25C084FC)
val GlassSurface = Color(0x18A855F7)

val PrimaryText = Color(0xFFF1E8FF)
val SecondaryText = Color(0xFF9B8EC4)
val LiveRed = Color(0xFFFF3B3B)
val ErrorRed = Color(0xFFEF4444)
val SuccessGreen = Color(0xFF22C55E)

val LocalStreamWaveColors = compositionLocalOf { PurpleTheme }
