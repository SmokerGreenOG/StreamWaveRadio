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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.streamwave.radio.R
import com.streamwave.radio.core.theme.*
import com.streamwave.radio.player.PlayingState
import com.streamwave.radio.player.RadioPlayer
import com.streamwave.radio.ui.components.AudioBars
import com.streamwave.radio.ui.components.PlaceholderLogo

@Composable
fun MiniPlayer(radioPlayer: RadioPlayer, onOpenFullPlayer: () -> Unit, modifier: Modifier = Modifier) {
    val state by radioPlayer.playerState.collectAsState()
    var showVolume by remember { mutableStateOf(false) }

    if (state.stationName.isEmpty()) return  // Niks tonen als er geen station speelt

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp), ambientColor = PurpleGlow, spotColor = Color(0x60A855F7))
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(ElevatedCard)
    ) {
        // Hoofdrij
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onOpenFullPlayer() }
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo
            if (state.stationLogo.isNotEmpty()) {
                AsyncImage(
                    model = state.stationLogo, contentDescription = null,
                    modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                PlaceholderLogo(state.stationName, size = 44)
            }

            Spacer(Modifier.width(10.dp))

            // Info
            Column(Modifier.weight(1f)) {
                Text(state.stationName, color = PrimaryText, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AudioBars(isPlaying = state.state == PlayingState.PLAYING)
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = when {
                            state.state == PlayingState.BUFFERING -> stringResource(R.string.buffering)
                            state.title.isNotEmpty() -> state.title
                            else -> stringResource(R.string.live)
                        },
                        color = if (state.state == PlayingState.BUFFERING) Pink else SecondaryText,
                        fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Volume
            IconButton(onClick = { showVolume = !showVolume }, modifier = Modifier.size(36.dp)) {
                Icon(
                    if (state.isMuted) Icons.Filled.VolumeOff
                    else if (state.volume > 1.0f) Icons.Filled.VolumeUp
                    else Icons.Filled.VolumeDown,
                    stringResource(R.string.volume),
                    tint = if (showVolume) Purple else SecondaryText,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Play/Pauze
            IconButton(onClick = {
                if (state.state == PlayingState.PLAYING) radioPlayer.pause()
                else radioPlayer.resume()
            }, modifier = Modifier.size(36.dp)) {
                Icon(
                    if (state.state == PlayingState.PLAYING) Icons.Default.Pause else Icons.Default.PlayArrow,
                    stringResource(if (state.state == PlayingState.PLAYING) R.string.pause else R.string.play),
                    tint = Purple, modifier = Modifier.size(24.dp)
                )
            }

            // Stop
            IconButton(onClick = { radioPlayer.stop() }, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Default.Stop, stringResource(R.string.stop), tint = ErrorRed, modifier = Modifier.size(20.dp))
            }
        }

        // Volume slider
        AnimatedVisibility(
            visible = showVolume,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.VolumeDown, null, tint = SecondaryText, modifier = Modifier.size(16.dp))
                Slider(
                    value = state.volume,
                    onValueChange = { radioPlayer.setVolume(it) },
                    modifier = Modifier.weight(1f),
                    valueRange = 0f..2f,
                    colors = SliderDefaults.colors(thumbColor = Purple, activeTrackColor = Purple, inactiveTrackColor = GlassBorder)
                )
                Icon(Icons.Filled.VolumeUp, null, tint = SecondaryText, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("${(state.volume * 100).toInt()}%", color = SecondaryText, fontSize = 11.sp)
            }
        }
    }
}
