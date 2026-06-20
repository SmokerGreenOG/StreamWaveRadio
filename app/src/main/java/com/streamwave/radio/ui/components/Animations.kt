package com.streamwave.radio.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.streamwave.radio.core.theme.*

// === Pulserende LIVE indicator ===
@Composable
fun PulseLive(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "scale"
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "alpha"
    )

    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .scale(scale)
                .clip(CircleShape)
                .background(LiveRed.copy(alpha = alpha))
        )
        Spacer(Modifier.width(4.dp))
        Text("LIVE", color = LiveRed, fontSize = 11.sp)
    }
}

// === Neon glow card ===
@Composable
fun GlowCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    glowColor: Color = PurpleGlow,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .shadow(8.dp, RoundedCornerShape(16.dp), ambientColor = glowColor, spotColor = glowColor)
            .clip(RoundedCornerShape(16.dp))
            .background(Card)
            .border(1.dp, NeonBorder, RoundedCornerShape(16.dp))
    ) {
        content()
    }
}

// === Animated audio bars ===
@Composable
fun AudioBars(isPlaying: Boolean, modifier: Modifier = Modifier) {
    val barCount = 5
    Row(
        modifier = modifier.height(24.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        for (i in 0 until barCount) {
            val infiniteTransition = rememberInfiniteTransition(label = "bar$i")
            val height by infiniteTransition.animateFloat(
                initialValue = 0.2f, targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(400 + i * 150, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = "h$i"
            )
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .fillMaxHeight(if (isPlaying) height else 0.15f)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Purple, Pink)
                        )
                    )
            )
        }
    }
}

// === Gradient border animatie ===
@Composable
fun GradientBorder(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "gradient")
    val gradientOffset by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Reverse),
        label = "offset"
    )
    Box(
        modifier = modifier
            .border(
                1.5.dp,
                Brush.sweepGradient(
                    listOf(Purple, Pink, Cyan, Purple),
                    Offset(gradientOffset, gradientOffset)
                ),
                RoundedCornerShape(16.dp)
            )
    ) {
        content()
    }
}
