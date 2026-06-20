package com.streamwave.radio.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.streamwave.radio.core.theme.*
import com.streamwave.radio.ui.components.PlaceholderLogo
import com.streamwave.radio.ui.components.SearchBar
import com.streamwave.radio.ui.components.StationCard
import com.streamwave.radio.ui.player.MiniPlayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpenFullPlayer: () -> Unit,
    onOpenFavorites: () -> Unit,
    onOpenSettings: () -> Unit,
    onAddPersonalStation: () -> Unit,
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
                containerColor = Purple,
                contentColor = PrimaryText
            ) {
                Icon(Icons.Default.Add, contentDescription = "Station toevoegen")
            }
        },
        bottomBar = {
            MiniPlayer(
                radioPlayer = viewModel.radioPlayer,
                onOpenFullPlayer = onOpenFullPlayer
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🎵 StreamWave Radio",
                        color = PrimaryText,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row {
                        IconButton(onClick = onOpenFavorites) {
                            Icon(Icons.Default.Favorite, "Favorieten", tint = Pink)
                        }
                        IconButton(onClick = onOpenSettings) {
                            Icon(Icons.Default.Settings, "Instellingen", tint = SecondaryText)
                        }
                    }
                }
            }

            // Search
            item {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { viewModel.onSearch(it) }
                )
            }

            // Now Playing
            if (playerState.stationName.isNotEmpty()) {
                item {
                    SectionTitle("Nu aan het luisteren")
                    NowPlayingCard(
                        stationName = playerState.stationName,
                        logoUrl = playerState.stationLogo,
                        artist = playerState.artist,
                        title = playerState.title,
                        isPlaying = playerState.state.name == "PLAYING",
                        onPlayPause = {
                            if (playerState.state.name == "PLAYING") viewModel.radioPlayer.pause()
                            else viewModel.radioPlayer.resume()
                        },
                        onStop = { viewModel.radioPlayer.stop() },
                        onOpenFull = onOpenFullPlayer
                    )
                }
            }

            // Featured
            if (featured.isNotEmpty()) {
                item {
                    SectionTitle("Uitgelichte stations")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(featured, key = { it.id }) { station ->
                            StationCard(
                                name = station.name,
                                logoUrl = station.logoUrl,
                                description = station.description,
                                isLive = true,
                                onClick = { viewModel.playStation(station) }
                            )
                        }
                    }
                }
            }

            // All Stations
            item { SectionTitle("Alle radiostations") }

            if (stations.isEmpty()) {
                item {
                    Box(
                        Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Stations laden…", color = SecondaryText)
                    }
                }
            }

            items(stations, key = { it.id }) { station ->
                StationListItem(
                    name = station.name,
                    logoUrl = station.logoUrl,
                    description = station.description,
                    country = station.country,
                    onClick = { viewModel.playStation(station) }
                )
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        color = LightPurple,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
    )
}

@Composable
fun NowPlayingCard(
    stationName: String, logoUrl: String, artist: String, title: String,
    isPlaying: Boolean, onPlayPause: () -> Unit, onStop: () -> Unit, onOpenFull: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ElevatedCard)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (logoUrl.isNotEmpty()) {
                AsyncImage(model = logoUrl, contentDescription = null,
                    modifier = Modifier.size(64.dp).clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop)
            } else { PlaceholderLogo(stationName, 64) }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(stationName, color = PrimaryText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(Modifier.width(8.dp))
                    Box(Modifier.clip(RoundedCornerShape(4.dp)).background(LiveRed).padding(horizontal = 6.dp, vertical = 2.dp)) {
                        Text("LIVE", color = PrimaryText, fontSize = 9.sp)
                    }
                }
                if (title.isNotEmpty()) {
                    Text("$artist — $title", color = SecondaryText, fontSize = 13.sp)
                }
                Spacer(Modifier.height(8.dp))
                Row {
                    IconButton(onClick = onPlayPause) {
                        Icon(if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, "Play/Pause", tint = Purple)
                    }
                    IconButton(onClick = onStop) {
                        Icon(Icons.Default.Stop, "Stop", tint = SecondaryText)
                    }
                }
            }
        }
    }
}

@Composable
fun StationListItem(
    name: String, logoUrl: String, description: String, country: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Card),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (logoUrl.isNotEmpty()) {
                AsyncImage(model = logoUrl, contentDescription = null,
                    modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop)
            } else { PlaceholderLogo(name, 48) }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(name, color = PrimaryText, fontWeight = FontWeight.Medium, fontSize = 15.sp)
                if (description.isNotEmpty()) {
                    Text(description, color = SecondaryText, fontSize = 12.sp, maxLines = 1)
                }
            }
            Icon(Icons.Default.PlayArrow, "Afspelen", tint = Purple)
        }
    }
}
