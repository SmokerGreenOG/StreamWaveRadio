package com.streamwave.radio.ui.settings

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.streamwave.radio.R
import com.streamwave.radio.core.theme.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.streamwave.radio.core.localization.LanguageManager
import com.streamwave.radio.ui.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit, onOpenAdmin: () -> Unit, onOpenMyStations: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val ctx = LocalContext.current
    var showLanguagePicker by remember { mutableStateOf(false) }
    var showThemePicker by remember { mutableStateOf(false) }
    val playerState by viewModel.radioPlayer.playerState.collectAsState()

    Scaffold(containerColor = Background,
        topBar = { TopAppBar(title = { Text(stringResource(R.string.settings), color = PrimaryText) },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back), tint = PrimaryText) } },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)) }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text(stringResource(R.string.volume), color = currentTheme.primaryLight)
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { viewModel.radioPlayer.setVolume(0f) }) { Icon(Icons.Filled.VolumeOff, stringResource(R.string.mute), tint = SecondaryText) }
                Slider(value = playerState.volume, onValueChange = { viewModel.radioPlayer.setVolume(it) }, modifier = Modifier.weight(1f), valueRange = 0f..2f,
                    colors = SliderDefaults.colors(thumbColor = currentTheme.primary, activeTrackColor = currentTheme.primary))
                IconButton(onClick = { viewModel.radioPlayer.setVolume(2f) }) { Icon(Icons.Filled.VolumeUp, "Max", tint = SecondaryText) }
                Text("${(playerState.volume * 100).toInt()}%", color = SecondaryText, fontSize = 12.sp)
            }
            HorizontalDivider(color = GlassBorder, modifier = Modifier.padding(vertical = 12.dp))

            // Thema
            SettingsRow(icon = Icons.Default.Palette, title = stringResource(R.string.theme_label), subtitle = "", onClick = { showThemePicker = true })
            SettingsRow(icon = Icons.Default.Language, title = stringResource(R.string.language), subtitle = "", onClick = { showLanguagePicker = true })
            SettingsRow(icon = Icons.Default.Radio, title = stringResource(R.string.my_stations), subtitle = "", onClick = onOpenMyStations)
            HorizontalDivider(color = GlassBorder, modifier = Modifier.padding(vertical = 12.dp))
            SettingsRow(icon = Icons.Default.AdminPanelSettings, title = stringResource(R.string.admin_login), subtitle = "", onClick = onOpenAdmin)
            HorizontalDivider(color = GlassBorder, modifier = Modifier.padding(vertical = 12.dp))
            Text(stringResource(R.string.about), color = currentTheme.primaryLight)
            Spacer(Modifier.height(8.dp))
            Text("StreamWave Radio", color = PrimaryText)
            Text("v1.0 — SmokerGreenOG", color = SecondaryText)
        }
    }

    if (showThemePicker) {
        AlertDialog(onDismissRequest = { showThemePicker = false }, containerColor = Panel,
            title = { Text(stringResource(R.string.select_theme), color = PrimaryText) },
            text = {
                Column { AllThemes.forEachIndexed { idx, theme ->
                    TextButton(onClick = {
                        applyTheme(idx)
                        val app = ctx.applicationContext as com.streamwave.radio.StreamWaveApp
                        kotlinx.coroutines.runBlocking { app.settingsDataStore.setTheme(idx.toString()) }
                        showThemePicker = false
                        (ctx as? Activity)?.recreate()
                    }, modifier = Modifier.fillMaxWidth()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(20.dp).background(theme.primary, RoundedCornerShape(10.dp)))
                            Spacer(Modifier.width(12.dp))
                            Text(stringResource(getThemeNameRes(idx)), color = PrimaryText, fontSize = 16.sp)
                        }
                    }
                } }
            },
            confirmButton = { TextButton(onClick = { showThemePicker = false }) { Text(stringResource(R.string.cancel), color = SecondaryText) } }
        )
    }

    if (showLanguagePicker) {
        AlertDialog(onDismissRequest = { showLanguagePicker = false }, containerColor = Panel,
            title = { Text(stringResource(R.string.select_language), color = PrimaryText) },
            text = { Column {
                listOf("nl" to "🇳🇱 Nederlands", "en" to "🇬🇧 English", "de" to "🇩🇪 Deutsch", "es" to "🇪🇸 Español").forEach { (code, label) ->
                    TextButton(onClick = {
                        val app = ctx.applicationContext as com.streamwave.radio.StreamWaveApp
                        kotlinx.coroutines.runBlocking { app.languageManager.setLanguage(code) }
                        showLanguagePicker = false; (ctx as? Activity)?.recreate()
                    }, modifier = Modifier.fillMaxWidth()) { Text(label, color = PrimaryText, fontSize = 16.sp) }
                } }
            },
            confirmButton = { TextButton(onClick = { showLanguagePicker = false }) { Text(stringResource(R.string.cancel), color = SecondaryText) } }
        )
    }
}

@Composable fun SettingsRow(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Surface(onClick = onClick, color = Card, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) { Icon(icon, null, tint = currentTheme.primary); Spacer(Modifier.width(16.dp)); Column(Modifier.weight(1f)) { Text(title, color = PrimaryText); if (subtitle.isNotEmpty()) Text(subtitle, color = SecondaryText, fontSize = 12.sp) }; Icon(Icons.Default.ChevronRight, null, tint = SecondaryText) }
    }
}
