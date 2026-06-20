package com.streamwave.radio.ui.player

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.streamwave.radio.core.theme.*
import com.streamwave.radio.player.PlayingState
import com.streamwave.radio.player.RadioPlayer
import com.streamwave.radio.ui.components.AudioBars
import com.streamwave.radio.ui.components.PlaceholderLogo

@Composable
fun MiniPlayer(
    radioPlayer: RadioPlayer,
    onOpenFullPlayer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by radioPlayer.playerState.collectAsState()
    var showVolume by remember { mutableStateOf(false) }

    AnimatedVisibility(
        visible = state.stationName.isNotEmpty(),
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .shadow(12.dp, ambientColor = PurpleGlow, spotColor = PurpleGlow)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(Brush.horizontalGradient(listOf(ElevatedCard, Card, ElevatedCard)))
        ) {
            // Hoofdrij
            Row(
                modifier = Modifier.fillMaxWidth().height(56.dp).clickable { onOpenFullPlayer() }.padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (state.stationLogo.isNotEmpty()) {
                    AsyncImage(model = state.stationLogo, contentDescription = null,
                        modifier = Modifier.size(42.dp).clip(RoundedCornerShape(10.dp)).shadow(4.dp, RoundedCornerShape(10.dp), ambientColor = PurpleGlow),
                        contentScale = ContentScale.Crop)
                } else { PlaceholderLogo(state.stationName, size = 42) }

                Spacer(Modifier.width(10.dp))

                Column(Modifier.weight(1f)) {
                    Text(state.stationName, color = PrimaryText, fontSize = 14.sp,
                        maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AudioBars(isPlaying = state.state == PlayingState.PLAYING)
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = when {
                                state.state == PlayingState.BUFFERING -> "Verbinden…"
                                state.title.isNotEmpty() -> "${state.artist} — ${state.title}"
                                else -> "LIVE"
                            },
                            color = if (state.state == PlayingState.BUFFERING) Pink else SecondaryText,
                            fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Volume toggle knop
                IconButton(onClick = { showVolume = !showVolume }) {
                    Icon(
                        if (state.isMuted) Icons.Filled.VolumeOff
                        else if (state.volume > 0.6f) Icons.Filled.VolumeUp
                        else Icons.Filled.VolumeDown,
                        "Volume", tint = if (showVolume) Purple else SecondaryText,
                        modifier = Modifier.size(22.dp)
                    )
                }

                IconButton(onClick = {
                    if (state.state == PlayingState.PLAYING) radioPlayer.pause()
                    else radioPlayer.resume()
                }) {
                    Icon(
                        if (state.state == PlayingState.PLAYING) Icons.Default.Pause else Icons.Default.PlayArrow,
                        "Play/Pause", tint = Purple, modifier = Modifier.size(26.dp)
                    )
                }
                IconButton(onClick = { radioPlayer.stop() }) {
                    Icon(Icons.Default.Stop, "Stop", tint = SecondaryText, modifier = Modifier.size(22.dp))
                }
            }

            // Uitklapbare volume slider
            AnimatedVisibility(
                visible = showVolume,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { radioPlayer.mute() }) {
                        Icon(if (state.isMuted) Icons.Filled.VolumeOff else Icons.Filled.VolumeDown,
                            "Mute", tint = SecondaryText, modifier = Modifier.size(20.dp))
                    }
                    Slider(
                        value = state.volume,
                        onValueChange = { radioPlayer.setVolume(it) },
                        modifier = Modifier.weight(1f),
                        valueRange = 0f..2f,
                        colors = SliderDefaults.colors(
                            thumbColor = Purple, activeTrackColor = Purple,
                            inactiveTrackColor = GlassBorder
                        )
                    )
                    Text("${(state.volume * 100).toInt()}%", color = SecondaryText, fontSize = 12.sp)
                }
            }
        }
    }
}
