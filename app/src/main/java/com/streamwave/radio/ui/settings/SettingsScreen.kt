package com.streamwave.radio.ui.settings

import android.app.Activity
import android.content.Intent
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
import com.streamwave.radio.MainActivity
import com.streamwave.radio.R
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
    val context = LocalContext.current
    var showLanguagePicker by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings), color = PrimaryText) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back), tint = PrimaryText) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            val playerState by homeViewModel.radioPlayer.playerState.collectAsState()
            
            Text(stringResource(R.string.volume), color = LightPurple)
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { homeViewModel.radioPlayer.setVolume(0f) }) {
                    Icon(Icons.Filled.VolumeOff, stringResource(R.string.mute), tint = SecondaryText)
                }
                Slider(value = playerState.volume, onValueChange = { homeViewModel.radioPlayer.setVolume(it) },
                    modifier = Modifier.weight(1f), valueRange = 0f..2f,
                    colors = SliderDefaults.colors(thumbColor = Purple, activeTrackColor = Purple))
                IconButton(onClick = { homeViewModel.radioPlayer.setVolume(2f) }) {
                    Icon(Icons.Filled.VolumeUp, "Max", tint = SecondaryText)
                }
                Text("${(playerState.volume * 100).toInt()}%", color = SecondaryText, fontSize = 12.sp)
            }

            HorizontalDivider(color = GlassBorder, modifier = Modifier.padding(vertical = 12.dp))
            SettingsRow(icon = Icons.Default.Language, title = stringResource(R.string.language), subtitle = "", onClick = { showLanguagePicker = true })
            SettingsRow(icon = Icons.Default.Radio, title = stringResource(R.string.my_stations), subtitle = "", onClick = onOpenMyStations)
            HorizontalDivider(color = GlassBorder, modifier = Modifier.padding(vertical = 12.dp))
            SettingsRow(icon = Icons.Default.AdminPanelSettings, title = stringResource(R.string.admin_login), subtitle = "", onClick = onOpenAdmin)
            HorizontalDivider(color = GlassBorder, modifier = Modifier.padding(vertical = 12.dp))

            Text(stringResource(R.string.about), color = LightPurple)
            Spacer(Modifier.height(8.dp))
            Text(Constants.APP_NAME, color = PrimaryText)
            Text(stringResource(R.string.version, Constants.VERSION), color = SecondaryText)
            Text(stringResource(R.string.made_by, Constants.MAKER), color = SecondaryText)
        }

        if (showLanguagePicker) {
            AlertDialog(
                onDismissRequest = { showLanguagePicker = false }, containerColor = Panel,
                title = { Text(stringResource(R.string.select_language), color = PrimaryText) },
                text = {
                    Column {
                        listOf("nl" to R.string.language_nl, "en" to R.string.language_en, "de" to R.string.language_de, "es" to R.string.language_es).forEach { (code, labelRes) ->
                            TextButton(onClick = {
                                // Direct toepassen (statische cache)
                                LanguageManager.applyLocale(context.applicationContext, code)
                                // Async opslaan in DataStore
                                val app = context.applicationContext as com.streamwave.radio.StreamWaveApp
                                kotlinx.coroutines.runBlocking { app.languageManager.setLanguage(code) }
                                showLanguagePicker = false
                                // Herstart
                                val i = Intent(context, MainActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                }
                                context.startActivity(i)
                                (context as? Activity)?.finish()
                            }, modifier = Modifier.fillMaxWidth()) {
                                Text(stringResource(labelRes), color = PrimaryText, fontSize = 16.sp)
                            }
                        }
                    }
                },
                confirmButton = { TextButton(onClick = { showLanguagePicker = false }) { Text(stringResource(R.string.cancel), color = SecondaryText) } }
            )
        }
    }
}

@Composable
fun SettingsRow(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Surface(onClick = onClick, color = Card, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = Purple)
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) { Text(title, color = PrimaryText) }
            Icon(Icons.Default.ChevronRight, null, tint = SecondaryText)
        }
    }
}
