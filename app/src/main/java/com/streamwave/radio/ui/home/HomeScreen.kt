package com.streamwave.radio.ui.home

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.streamwave.radio.core.theme.*
import com.streamwave.radio.player.PlayingState
import com.streamwave.radio.ui.components.*
import com.streamwave.radio.ui.player.MiniPlayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpenFullPlayer: () -> Unit, onOpenFavorites: () -> Unit,
    onOpenSettings: () -> Unit, onAddPersonalStation: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val stations by viewModel.officialStations.collectAsState()
    val featured by viewModel.featuredStations.collectAsState()
    val playerState by viewModel.radioPlayer.playerState.collectAsState()

    Scaffold(
        containerColor = Background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddPersonalStation,
                containerColor = Purple, contentColor = PrimaryText,
                modifier = Modifier.shadow(12.dp, CircleShape, ambientColor = PurpleGlow, spotColor = PurpleGlow)
            ) { Icon(Icons.Default.Add, "Station toevoegen") }
        },
        bottomBar = { MiniPlayer(radioPlayer = viewModel.radioPlayer, onOpenFullPlayer = onOpenFullPlayer) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { Spacer(Modifier.height(4.dp)) }

            // Header
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🎵", fontSize = 24.sp)
                        Spacer(Modifier.width(8.dp))
                        Text("StreamWave Radio", color = PrimaryText, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }
                    Row {
                        IconButton(onClick = onOpenFavorites) { Icon(Icons.Default.Favorite, "Favorieten", tint = Pink) }
                        IconButton(onClick = onOpenSettings) { Icon(Icons.Default.Settings, "Instellingen", tint = SecondaryText) }
                    }
                }
            }

            item { SearchBar(query = searchQuery, onQueryChange = { viewModel.onSearch(it) }) }

            // Now Playing
            if (playerState.stationName.isNotEmpty()) {
                item {
                    SectionTitle("Nu aan het luisteren")
                    GlowCard(onClick = {}, glowColor = PinkGlow) {
                        Column(Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (playerState.stationLogo.isNotEmpty())
                                    AsyncImage(model = playerState.stationLogo, contentDescription = null,
                                        modifier = Modifier.size(56.dp).clip(RoundedCornerShape(14.dp)).shadow(6.dp, RoundedCornerShape(14.dp), ambientColor = PinkGlow),
                                        contentScale = ContentScale.Crop)
                                else PlaceholderLogo(playerState.stationName, 56)
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(playerState.stationName, color = PrimaryText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                        Spacer(Modifier.width(8.dp))
                                        PulseLive()
                                    }
                                    if (playerState.title.isNotEmpty())
                                        Text("${playerState.artist} — ${playerState.title}", color = SecondaryText, fontSize = 13.sp)
                                    Spacer(Modifier.height(4.dp))
                                    AudioBars(isPlaying = playerState.state == PlayingState.PLAYING)
                                }
                            }

                            Spacer(Modifier.height(10.dp))

                            // Controls: play/pauze + stop + volume
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                // Play/Pauze
                                FilledIconButton(
                                    onClick = {
                                        if (playerState.state == PlayingState.PLAYING) viewModel.radioPlayer.pause()
                                        else viewModel.radioPlayer.resume()
                                    },
                                    modifier = Modifier.size(42.dp),
                                    shape = CircleShape,
                                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = Purple)
                                ) {
                                    Icon(
                                        if (playerState.state == PlayingState.PLAYING) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        "Play/Pause", modifier = Modifier.size(22.dp)
                                    )
                                }

                                // Stop
                                IconButton(onClick = { viewModel.radioPlayer.stop() }) {
                                    Icon(Icons.Default.Stop, "Stop", tint = ErrorRed, modifier = Modifier.size(26.dp))
                                }

                                Spacer(Modifier.width(4.dp))

                                // Volume
                                Icon(
                                    if (playerState.isMuted) Icons.Filled.VolumeOff
                                    else if (playerState.volume > 0.6f) Icons.Filled.VolumeUp
                                    else Icons.Filled.VolumeDown,
                                    "Volume", tint = SecondaryText, modifier = Modifier.size(18.dp)
                                )
                                Slider(
                                    value = playerState.volume,
                                    onValueChange = { viewModel.radioPlayer.setVolume(it) },
                                    modifier = Modifier.weight(1f),
                                    colors = SliderDefaults.colors(thumbColor = Purple, activeTrackColor = Purple, inactiveTrackColor = GlassBorder)
                                )
                                Text("${(playerState.volume * 100).toInt()}%", color = SecondaryText, fontSize = 11.sp)

                                // Open full player
                                IconButton(onClick = onOpenFullPlayer) {
                                    Icon(Icons.Default.OpenInFull, "Volledige speler", tint = SecondaryText, modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                    }
                }
            }

            // Featured
            if (featured.isNotEmpty()) {
                item { SectionTitle("⭐ Uitgelichte stations") }
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(featured, key = { it.id }) { station ->
                            StationCard(name = station.name, logoUrl = station.logoUrl, description = station.description,
                                isLive = true, onClick = { viewModel.playStation(station) })
                        }
                    }
                }
            }

            // All Stations
            item { SectionTitle("📻 Alle radiostations") }

            if (stations.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                        Text("Stations laden…", color = SecondaryText)
                    }
                }
            }

            items(stations, key = { it.id }) { station ->
                StationListItem(name = station.name, logoUrl = station.logoUrl, description = station.description,
                    onClick = { viewModel.playStation(station) })
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(title, color = LightPurple, fontSize = 15.sp, fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(top = 4.dp, bottom = 2.dp))
}

@Composable
fun StationListItem(name: String, logoUrl: String, description: String, onClick: () -> Unit) {
    Surface(onClick = onClick, modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(14.dp), ambientColor = PurpleGlow),
        shape = RoundedCornerShape(14.dp), color = Card
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            if (logoUrl.isNotEmpty())
                AsyncImage(model = logoUrl, contentDescription = null, modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp)), contentScale = ContentScale.Crop)
            else PlaceholderLogo(name, 44)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(name, color = PrimaryText, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                if (description.isNotEmpty()) Text(description, color = SecondaryText, fontSize = 11.sp, maxLines = 1)
            }
            Icon(Icons.Default.PlayArrow, "Afspelen", tint = Purple, modifier = Modifier.size(22.dp))
        }
    }
}
