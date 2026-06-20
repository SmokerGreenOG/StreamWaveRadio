package com.streamwave.radio.ui.player

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.streamwave.radio.R
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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back), tint = PrimaryText)
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
                        stringResource(R.string.mute), tint = SecondaryText)
                }
                Slider(value = state.volume, onValueChange = { viewModel.radioPlayer.setVolume(it) },
                    modifier = Modifier.weight(1f), valueRange = 0f..2f,
                    colors = SliderDefaults.colors(thumbColor = Purple, activeTrackColor = Purple, inactiveTrackColor = GlassBorder))
                Text("${(state.volume * 100).toInt()}%", color = SecondaryText, fontSize = 12.sp)
            }

            Spacer(Modifier.height(16.dp))

            // Extra knoppen
            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.FavoriteBorder, stringResource(R.string.favorite), tint = Pink, modifier = Modifier.size(28.dp))
                }
                IconButton(onClick = { showSleepTimer = true }) {
                    Icon(Icons.Filled.Bedtime, stringResource(R.string.sleep_timer), tint = Cyan, modifier = Modifier.size(28.dp))
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Share, stringResource(R.string.share), tint = SecondaryText, modifier = Modifier.size(28.dp))
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

// === Meerdere visualizer stijlen ===
@Composable
fun LargeVisualizer(isPlaying: Boolean) {
    var style by remember { mutableIntStateOf(0) }
    val styles = 4

    Box(modifier = Modifier.fillMaxWidth().height(52.dp).clickable { style = (style + 1) % styles },
        contentAlignment = Alignment.Center) {
        when (style) {
            0 -> BarVisualizer(isPlaying, 32)
            1 -> CircleVisualizer(isPlaying)
            2 -> WaveVisualizer(isPlaying)
            else -> DotVisualizer(isPlaying)
        }
    }
}

@Composable
private fun BarVisualizer(isPlaying: Boolean, count: Int = 32) {
    Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(2.dp), verticalAlignment = Alignment.CenterVertically) {
        for (i in 0 until count) {
            val t = rememberInfiniteTransition(label = "bv$i")
            val h by t.animateFloat(0.08f, 1f, infiniteRepeatable(tween(300 + i * 80, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "h")
            Box(Modifier.weight(1f).fillMaxHeight(if (isPlaying) h else 0.08f).clip(RoundedCornerShape(1.dp)).background(Brush.verticalGradient(listOf(Purple, Pink, Cyan))))
        }
    }
}

@Composable
private fun CircleVisualizer(isPlaying: Boolean) {
    Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
        for (i in 0 until 8) {
            val t = rememberInfiniteTransition(label = "cv$i")
            val s by t.animateFloat(0.3f, 1f, infiniteRepeatable(tween(400 + i * 120, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "s")
            Box(Modifier.size((20 + i * 4).dp).scale(if (isPlaying) s else 0.3f).clip(CircleShape).background(Brush.horizontalGradient(listOf(Purple, Cyan))))
        }
    }
}

@Composable
private fun WaveVisualizer(isPlaying: Boolean) {
    val t = rememberInfiniteTransition(label = "wv")
    val offset by t.animateFloat(0f, 1f, infiniteRepeatable(tween(1500, easing = LinearEasing), RepeatMode.Restart), label = "o")
    Canvas(Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val amp = if (isPlaying) h * 0.4f else h * 0.05f
        val path = androidx.compose.ui.graphics.Path()
        path.moveTo(0f, h / 2)
        for (x in 0..w.toInt() step 4) {
            val y = h / 2 + amp * kotlin.math.sin((x / w * 4 * Math.PI + offset * 2 * Math.PI).toFloat())
            path.lineTo(x.toFloat(), y)
        }
        drawPath(path, color = Purple, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3f))
        drawPath(path, color = Cyan, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.5f), alpha = 0.5f)
    }
}

@Composable
private fun DotVisualizer(isPlaying: Boolean) {
    Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
        for (i in 0 until 16) {
            val t = rememberInfiniteTransition(label = "dv$i")
            val a by t.animateFloat(0.2f, 1f, infiniteRepeatable(tween(250 + i * 100, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "a")
            Box(Modifier.size(8.dp).alpha(if (isPlaying) a else 0.2f).clip(CircleShape).background(if (i % 2 == 0) Purple else Pink))
        }
    }
}
