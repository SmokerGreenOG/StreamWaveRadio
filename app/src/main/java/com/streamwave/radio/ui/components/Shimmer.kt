package com.streamwave.radio.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.streamwave.radio.core.theme.*

@Composable
fun ShimmerBox(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(1200, easing = LinearEasing), RepeatMode.Restart),
        label = "shimmer"
    )
    val brush = Brush.linearGradient(
        colors = listOf(Card, ElevatedCard, Card),
        start = Offset(translateAnim - 200f, 0f),
        end = Offset(translateAnim, 0f)
    )
    Box(modifier.clip(RoundedCornerShape(8.dp)).background(brush))
}

@Composable
fun StationShimmer(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 6.dp, horizontal = 4.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        ShimmerBox(Modifier.size(44.dp))
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            ShimmerBox(Modifier.fillMaxWidth(0.7f).height(14.dp))
            Spacer(Modifier.height(6.dp))
            ShimmerBox(Modifier.fillMaxWidth(0.4f).height(11.dp))
        }
        ShimmerBox(Modifier.size(24.dp))
    }
}

@Composable
fun LoadingShimmerList(itemCount: Int = 8) {
    Column {
        for (i in 0 until itemCount) {
            StationShimmer()
        }
    }
}
