package com.streamwave.radio.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.streamwave.radio.core.theme.*

@Composable
fun StationCard(
    name: String, logoUrl: String, description: String,
    isLive: Boolean = false, isOfficial: Boolean = true,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .width(150.dp)
            .shadow(10.dp, RoundedCornerShape(16.dp), ambientColor = PurpleGlow, spotColor = PurpleGlow)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Card, ElevatedCard)
                )
            )
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Box(modifier = Modifier.size(80.dp), contentAlignment = Alignment.Center) {
                if (logoUrl.isNotEmpty()) {
                    AsyncImage(model = logoUrl, contentDescription = name,
                        modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp)).shadow(6.dp, RoundedCornerShape(12.dp), ambientColor = PurpleGlow),
                        contentScale = ContentScale.Crop)
                } else {
                    PlaceholderLogo(name = name, size = 80)
                }
                if (isLive) {
                    PulseLive(modifier = Modifier.align(Alignment.TopEnd).offset(x = 4.dp, y = (-4).dp))
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(name, color = PrimaryText, fontSize = 13.sp, fontWeight = FontWeight.SemiBold,
                maxLines = 2, overflow = TextOverflow.Ellipsis)
            if (!isOfficial) {
                Text("Persoonlijk", color = Pink, fontSize = 10.sp)
            }
        }
    }
}

@Composable
fun PlaceholderLogo(name: String, size: Int = 100) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.linearGradient(listOf(DarkPurple, Purple))
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(name.take(2).uppercase(), color = LightPurple, fontSize = (size / 3).sp, fontWeight = FontWeight.Bold)
    }
}
