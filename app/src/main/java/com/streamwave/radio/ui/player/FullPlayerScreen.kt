package com.streamwave.radio.ui.player

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.streamwave.radio.core.theme.*
import com.streamwave.radio.player.PlayingState
import com.streamwave.radio.ui.components.*
import com.streamwave.radio.ui.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullPlayerScreen(onBack: () -> Unit, viewModel: HomeViewModel = hiltViewModel()) {
    val state by viewModel.radioPlayer.playerState.collectAsState()
    var showSleepTimer by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text(state.stationName.ifEmpty { "StreamWave" }, color = PrimaryText) },
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
            modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Groot logo met glow
            Box(
                modifier = Modifier.size(220.dp).shadow(24.dp, CircleShape, ambientColor = PurpleGlow, spotColor = PurpleGlow),
                contentAlignment = Alignment.Center
            ) {
                if (state.stationLogo.isNotEmpty()) {
                    AsyncImage(model = state.stationLogo, contentDescription = null,
                        modifier = Modifier.size(200.dp).clip(RoundedCornerShape(28.dp)).shadow(12.dp, RoundedCornerShape(28.dp), ambientColor = PinkGlow),
                        contentScale = ContentScale.Crop)
                } else { PlaceholderLogo(state.stationName, 200) }
            }

            Spacer(Modifier.height(20.dp))

            // Naam + LIVE
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(state.stationName, color = PrimaryText, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(12.dp))
                PulseLive()
            }

            // Visualizer
            Spacer(Modifier.height(12.dp))
            LargeVisualizer(isPlaying = state.state == PlayingState.PLAYING)

            // Artist/Title
            if (state.title.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text("${state.artist} — ${state.title}", color = SecondaryText, fontSize = 16.sp)
            } else if (state.state == PlayingState.BUFFERING) {
                Spacer(Modifier.height(8.dp))
                Text("Verbinden…", color = Pink, fontSize = 14.sp)
            }

            Spacer(Modifier.height(28.dp))

            // Hoofdcontrols
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.SkipPrevious, "Vorige", tint = SecondaryText, modifier = Modifier.size(36.dp))
                }

                // Play/Pauze — grote neon knop
                Box(
                    modifier = Modifier.size(80.dp).shadow(16.dp, CircleShape, ambientColor = PurpleGlow, spotColor = PurpleGlow),
                    contentAlignment = Alignment.Center
                ) {
                    FilledIconButton(
                        onClick = {
                            if (state.state == PlayingState.PLAYING) viewModel.radioPlayer.pause()
                            else viewModel.radioPlayer.resume()
                        },
                        modifier = Modifier.size(72.dp),
                        shape = CircleShape,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = Purple,
                            contentColor = PrimaryText
                        )
                    ) {
                        Icon(
                            if (state.state == PlayingState.PLAYING) Icons.Default.Pause else Icons.Default.PlayArrow,
                            "Play/Pause", modifier = Modifier.size(36.dp)
                        )
                    }
                }

                IconButton(onClick = { viewModel.radioPlayer.stop() }) {
                    Icon(Icons.Default.Stop, "Stop", tint = SecondaryText, modifier = Modifier.size(36.dp))
                }
            }

            Spacer(Modifier.height(24.dp))

            // Volume
            Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { viewModel.radioPlayer.mute() }) {
                    Icon(if (state.isMuted) Icons.Filled.VolumeOff else Icons.Filled.VolumeDown,
                        "Mute", tint = SecondaryText)
                }
                Slider(value = state.volume, onValueChange = { viewModel.radioPlayer.setVolume(it) },
                    modifier = Modifier.weight(1f),
                    colors = SliderDefaults.colors(thumbColor = Purple, activeTrackColor = Purple, inactiveTrackColor = GlassBorder))
                Text("${(state.volume * 100).toInt()}%", color = SecondaryText, fontSize = 12.sp)
            }

            Spacer(Modifier.height(16.dp))

            // Extra knoppen
            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.FavoriteBorder, "Favoriet", tint = Pink, modifier = Modifier.size(28.dp))
                }
                IconButton(onClick = { showSleepTimer = true }) {
                    Icon(Icons.Filled.Bedtime, "Slaaptimer", tint = Cyan, modifier = Modifier.size(28.dp))
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Share, "Delen", tint = SecondaryText, modifier = Modifier.size(28.dp))
                }
            }
        }

        if (showSleepTimer) {
            SleepTimerDialogFull(
                sleepTimerManager = viewModel.radioPlayer.sleepTimerManager,
                onDismiss = { showSleepTimer = false },
                onStop = { viewModel.radioPlayer.stop() }
            )
        }
    }
}

// === Grotere audio visualizer ===
@Composable
fun LargeVisualizer(isPlaying: Boolean) {
    val barCount = 32
    Row(
        modifier = Modifier.fillMaxWidth().height(48.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until barCount) {
            val transition = rememberInfiniteTransition(label = "v$i")
            val height by transition.animateFloat(
                initialValue = 0.1f, targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(300 + i * 80, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = "h$i"
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(if (isPlaying) height else 0.08f)
                    .clip(RoundedCornerShape(1.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Purple, Pink, Cyan)
                        )
                    )
            )
        }
    }
}
