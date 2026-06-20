package com.streamwave.radio.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.streamwave.radio.core.common.Constants
import com.streamwave.radio.core.localization.LanguageManager
import com.streamwave.radio.core.theme.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.streamwave.radio.ui.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onOpenAdmin: () -> Unit,
    onOpenMyStations: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    var showLanguagePicker by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text("Instellingen", color = PrimaryText) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Terug", tint = PrimaryText) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            // Volume
            val playerState by homeViewModel.radioPlayer.playerState.collectAsState()
            Text("Volume", color = LightPurple)
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                IconButton(onClick = { homeViewModel.radioPlayer.setVolume(0f) }) {
                    Icon(Icons.Default.VolumeOff, "Mute", tint = SecondaryText)
                }
                Slider(
                    value = playerState.volume,
                    onValueChange = { homeViewModel.radioPlayer.setVolume(it) },
                    modifier = Modifier.weight(1f), valueRange = 0f..2f,
                    colors = SliderDefaults.colors(thumbColor = Purple, activeTrackColor = Purple)
                )
                IconButton(onClick = { homeViewModel.radioPlayer.setVolume(1f) }) {
                    Icon(Icons.Default.VolumeUp, "Max", tint = SecondaryText)
                }
                Text("${(playerState.volume * 100).toInt()}%", color = SecondaryText, fontSize = 12.sp)
            }

            Divider(color = GlassBorder, modifier = Modifier.padding(vertical = 12.dp))

            // Language
            SettingsRow(icon = Icons.Default.Language, title = "Taal", subtitle = "Nederlands",
                onClick = { showLanguagePicker = true })

            // My Stations
            SettingsRow(icon = Icons.Default.Radio, title = "Mijn stations", subtitle = "Persoonlijke radiostations",
                onClick = onOpenMyStations)

            Divider(color = GlassBorder, modifier = Modifier.padding(vertical = 12.dp))

            // Admin
            SettingsRow(icon = Icons.Default.AdminPanelSettings, title = "Beheerder", subtitle = "Admin paneel",
                onClick = onOpenAdmin)

            Divider(color = GlassBorder, modifier = Modifier.padding(vertical = 12.dp))

            // About
            Text("Over", color = LightPurple)
            Spacer(Modifier.height(8.dp))
            Text(Constants.APP_NAME, color = PrimaryText)
            Text("Versie ${Constants.VERSION}", color = SecondaryText)
            Text("Gemaakt door ${Constants.MAKER}", color = SecondaryText)
        }

        if (showLanguagePicker) {
            LanguagePickerDialog(
                onSelect = { showLanguagePicker = false },
                onDismiss = { showLanguagePicker = false }
            )
        }
    }
}

@Composable
fun SettingsRow(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick, color = Card, shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Icon(icon, null, tint = Purple)
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = PrimaryText)
                Text(subtitle, color = SecondaryText, fontSize = 12.sp)
            }
            Icon(Icons.Default.ChevronRight, null, tint = SecondaryText)
        }
    }
}

@Composable
fun LanguagePickerDialog(onSelect: (String) -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Panel,
        title = { Text("Kies taal", color = PrimaryText) },
        text = {
            Column {
                listOf(
                    "nl" to "🇳🇱 Nederlands",
                    "en" to "🇬🇧 English",
                    "de" to "🇩🇪 Deutsch",
                    "es" to "🇪🇸 Español"
                ).forEach { (code, label) ->
                    TextButton(onClick = { onSelect(code) }, modifier = Modifier.fillMaxWidth()) {
                        Text(label, color = PrimaryText, fontSize = 16.sp)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Annuleren", color = SecondaryText) }
        }
    )
}
