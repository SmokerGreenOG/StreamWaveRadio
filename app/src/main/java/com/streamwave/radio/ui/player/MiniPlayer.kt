package com.streamwave.radio.ui.player

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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

    AnimatedVisibility(
        visible = state.stationName.isNotEmpty(),
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .shadow(12.dp, ambientColor = PurpleGlow, spotColor = PurpleGlow)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(ElevatedCard, Card, ElevatedCard)
                    )
                )
                .clickable { onOpenFullPlayer() }
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (state.stationLogo.isNotEmpty()) {
                    AsyncImage(model = state.stationLogo, contentDescription = null,
                        modifier = Modifier.size(42.dp).clip(RoundedCornerShape(10.dp)).shadow(4.dp, RoundedCornerShape(10.dp), ambientColor = PurpleGlow),
                        contentScale = ContentScale.Crop)
                } else {
                    PlaceholderLogo(state.stationName, size = 42)
                }

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

                IconButton(onClick = {
                    if (state.state == PlayingState.PLAYING) radioPlayer.pause()
                    else radioPlayer.resume()
                }) {
                    Icon(
                        if (state.state == PlayingState.PLAYING) Icons.Default.Pause else Icons.Default.PlayArrow,
                        "Play/Pause", tint = Purple, modifier = Modifier.size(28.dp)
                    )
                }
                IconButton(onClick = { radioPlayer.stop() }) {
                    Icon(Icons.Default.Stop, "Stop", tint = SecondaryText, modifier = Modifier.size(24.dp))
                }
            }
        }
    }
}
