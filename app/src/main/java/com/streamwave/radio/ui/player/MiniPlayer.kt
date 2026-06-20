package com.streamwave.radio.ui.player

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.streamwave.radio.core.theme.*
import com.streamwave.radio.player.PlayingState
import com.streamwave.radio.player.RadioPlayer
import com.streamwave.radio.ui.components.PlaceholderLogo

@Composable
fun MiniPlayer(
    radioPlayer: RadioPlayer,
    onOpenFullPlayer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by radioPlayer.playerState.collectAsState()

    AnimatedVisibility(
        visible = state.stationName.isNotEmpty()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(ElevatedCard)
                .clickable { onOpenFullPlayer() }
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo
            if (state.stationLogo.isNotEmpty()) {
                AsyncImage(
                    model = state.stationLogo,
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                PlaceholderLogo(state.stationName, size = 48)
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = state.stationName,
                    color = PrimaryText,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = if (state.title.isNotEmpty()) "${state.artist} — ${state.title}"
                    else if (state.state == PlayingState.BUFFERING) "Verbinden…"
                    else "LIVE",
                    color = if (state.state == PlayingState.BUFFERING) Pink else SecondaryText,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // LIVE indicator
            if (state.state == PlayingState.PLAYING) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(LiveRed)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("LIVE", color = PrimaryText, fontSize = 9.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            // Play/Pause
            IconButton(onClick = {
                if (state.state == PlayingState.PLAYING) radioPlayer.pause()
                else radioPlayer.resume()
            }) {
                Icon(
                    imageVector = if (state.state == PlayingState.PLAYING) Icons.Default.Pause
                    else Icons.Default.PlayArrow,
                    contentDescription = "Play/Pause",
                    tint = Purple
                )
            }

            // Stop
            IconButton(onClick = { radioPlayer.stop() }) {
                Icon(
                    imageVector = Icons.Default.Stop,
                    contentDescription = "Stop",
                    tint = SecondaryText
                )
            }
        }
    }
}
