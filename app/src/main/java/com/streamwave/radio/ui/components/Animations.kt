package com.streamwave.radio.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.streamwave.radio.core.theme.*

@Composable
fun PulseLive(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "pulse")
    val alpha by transition.animateFloat(0.6f, 1f, infiniteRepeatable(tween(800), RepeatMode.Reverse), label = "a")
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Box(Modifier.size(8.dp).scale(1f).clip(CircleShape).background(LiveRed.copy(alpha = alpha)))
        Spacer(Modifier.width(4.dp))
        Text("LIVE", color = LiveRed, fontSize = 11.sp)
    }
}

@Composable
fun GlowCard(onClick: () -> Unit, modifier: Modifier = Modifier, glowColor: Color = PurpleGlow, content: @Composable () -> Unit) {
    Box(modifier.shadow(8.dp, RoundedCornerShape(16.dp), ambientColor = glowColor, spotColor = glowColor)
        .clip(RoundedCornerShape(16.dp)).background(Card)) { content() }
}

@Composable
fun AudioBars(isPlaying: Boolean, modifier: Modifier = Modifier) {
    if (!isPlaying) {
        // Toon minimale balkjes als niet aan het spelen
        Row(modifier.height(16.dp), horizontalArrangement = Arrangement.spacedBy(2.dp), verticalAlignment = Alignment.Bottom) {
            for (i in 0 until 5) {
                Box(Modifier.width(2.dp).fillMaxHeight(0.2f).clip(RoundedCornerShape(1.dp)).background(GlassBorder))
            }
        }
        return
    }
    Row(modifier.height(16.dp), horizontalArrangement = Arrangement.spacedBy(2.dp), verticalAlignment = Alignment.Bottom) {
        for (i in 0 until 5) {
            val t = rememberInfiniteTransition(label = "bar$i")
            val h by t.animateFloat(0.15f, 1f, infiniteRepeatable(tween(350 + i * 120, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "h")
            Box(Modifier.width(2.dp).fillMaxHeight(h).clip(RoundedCornerShape(1.dp))
                .background(Brush.verticalGradient(listOf(Purple, Pink))))
        }
    }
}
