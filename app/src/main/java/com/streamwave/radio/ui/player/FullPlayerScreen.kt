package com.streamwave.radio.ui.player

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.streamwave.radio.core.theme.*
import com.streamwave.radio.player.PlayingState
import com.streamwave.radio.ui.components.PlaceholderLogo
import com.streamwave.radio.ui.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullPlayerScreen(
    onBack: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.radioPlayer.playerState.collectAsState()
    var showSleepTimer by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Terug", tint = PrimaryText)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Box(modifier = Modifier.size(200.dp), contentAlignment = Alignment.Center) {
                if (state.stationLogo.isNotEmpty()) {
                    AsyncImage(
                        model = state.stationLogo, contentDescription = null,
                        modifier = Modifier.size(200.dp).clip(RoundedCornerShape(24.dp)),
                        contentScale = ContentScale.Crop)
                } else { PlaceholderLogo(state.stationName, 200) }
            }

            Spacer(Modifier.height(24.dp))

            // Name + Live
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(state.stationName, color = PrimaryText, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(12.dp))
                Box(Modifier.clip(RoundedCornerShape(4.dp)).background(LiveRed).padding(horizontal = 8.dp, vertical = 3.dp)) {
                    Text("LIVE", color = PrimaryText, fontSize = 11.sp)
                }
            }

            // Artist - Title
            if (state.title.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text("${state.artist} — ${state.title}", color = SecondaryText, fontSize = 16.sp)
            } else if (state.state == PlayingState.BUFFERING) {
                Spacer(Modifier.height(8.dp))
                Text("Verbinden…", color = Pink, fontSize = 14.sp)
            }

            // Audio visualizer placeholder
            Spacer(Modifier.height(16.dp))
            Box(
                Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)).background(GlassBorder)
            ) {
                if (state.state == PlayingState.PLAYING) {
                    Box(Modifier.fillMaxWidth(0.7f).fillMaxHeight().background(Purple))
                }
            }

            Spacer(Modifier.height(24.dp))

            // Controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.SkipPrevious, "Vorige", tint = SecondaryText, modifier = Modifier.size(32.dp))
                }
                FilledIconButton(
                    onClick = {
                        if (state.state == PlayingState.PLAYING) viewModel.radioPlayer.pause()
                        else viewModel.radioPlayer.resume()
                    },
                    modifier = Modifier.size(72.dp),
                    shape = RoundedCornerShape(36.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = Purple)
                ) {
                    Icon(
                        if (state.state == PlayingState.PLAYING) Icons.Default.Pause else Icons.Default.PlayArrow,
                        "Play/Pause", modifier = Modifier.size(36.dp)
                    )
                }
                IconButton(onClick = { viewModel.radioPlayer.stop() }) {
                    Icon(Icons.Default.Stop, "Stop", tint = SecondaryText, modifier = Modifier.size(32.dp))
                }
            }

            Spacer(Modifier.height(24.dp))

            // Extra controls
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                IconButton(onClick = { viewModel.radioPlayer.mute() }) {
                    Icon(if (state.isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp, "Volume", tint = SecondaryText)
                }
                IconButton(onClick = { showSleepTimer = true }) {
                    Icon(Icons.Default.Bedtime, "Slaaptimer", tint = Pink)
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Default.FavoriteBorder, "Favoriet", tint = Pink)
                }
            }
        }

        if (showSleepTimer) {
            SleepTimerDialog(
                currentMinutes = 0,
                onSelect = { mins ->
                    // Start sleep timer
                    showSleepTimer = false
                },
                onDismiss = { showSleepTimer = false }
            )
        }
    }
}

@Composable
fun SleepTimerDialog(
    currentMinutes: Int,
    onSelect: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Panel,
        title = { Text("Slaaptimer", color = PrimaryText) },
        text = {
            Column {
                listOf(10, 20, 30, 45, 60, 90).forEach { mins ->
                    TextButton(onClick = { onSelect(mins) }, modifier = Modifier.fillMaxWidth()) {
                        Text("$mins minuten", color = PrimaryText, fontSize = 16.sp)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuleren", color = SecondaryText)
            }
        }
    )
}
