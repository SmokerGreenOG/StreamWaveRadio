package com.streamwave.radio.ui.home

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.streamwave.radio.R
import com.streamwave.radio.core.theme.*
import com.streamwave.radio.player.PlayingState
import com.streamwave.radio.ui.components.*
import com.streamwave.radio.ui.player.MiniPlayer
import com.streamwave.radio.ui.player.SleepTimerDialogFull

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
    val showSleepTimer by viewModel.showSleepTimer.collectAsState()

    val isPlaying = playerState.state == PlayingState.PLAYING
    val isBuffering = playerState.state == PlayingState.BUFFERING

    Scaffold(
        containerColor = Background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddPersonalStation,
                containerColor = Purple, contentColor = PrimaryText,
                modifier = Modifier.shadow(12.dp, CircleShape, ambientColor = PurpleGlow)
            ) { Icon(Icons.Default.Add, stringResource(R.string.add_station)) }
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
                        Image(painter = painterResource(R.drawable.app_logo_small), contentDescription = null,
                            modifier = Modifier.size(34.dp).clip(RoundedCornerShape(8.dp)), contentScale = ContentScale.Fit)
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.app_name), color = PrimaryText, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }
                    Row {
                        IconButton(onClick = onOpenFavorites) { Icon(Icons.Default.Favorite, stringResource(R.string.favorites), tint = Pink) }
                        IconButton(onClick = onOpenSettings) { Icon(Icons.Default.Settings, stringResource(R.string.settings), tint = SecondaryText) }
                    }
                }
            }

            item { SearchBar(query = searchQuery, onQueryChange = { viewModel.onSearch(it) }) }

            // Now Playing — alleen als er een station actief is
            if (playerState.stationName.isNotEmpty()) {
                item {
                    SectionTitle(stringResource(R.string.now_playing))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = ElevatedCard)
                    ) {
                        Column(Modifier.padding(14.dp)) {
                            // Bovenste rij: logo + naam
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (playerState.stationLogo.isNotEmpty())
                                    AsyncImage(model = playerState.stationLogo, contentDescription = null,
                                        modifier = Modifier.size(52.dp).clip(RoundedCornerShape(14.dp)),
                                        contentScale = ContentScale.Crop)
                                else PlaceholderLogo(playerState.stationName, 52)
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(playerState.stationName, color = PrimaryText, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                        Spacer(Modifier.width(8.dp))
                                        PulseLive()
                                    }
                                    if (playerState.title.isNotEmpty())
                                        Text(playerState.title, color = SecondaryText, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    else if (isBuffering)
                                        Text(stringResource(R.string.buffering), color = Pink, fontSize = 13.sp)
                                    Spacer(Modifier.height(2.dp))
                                    AudioBars(isPlaying = isPlaying)
                                }
                            }

                            Spacer(Modifier.height(10.dp))

                            // Controls rij
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                // Play/Pauze
                                FilledIconButton(
                                    onClick = { if (isPlaying) viewModel.radioPlayer.pause() else viewModel.radioPlayer.resume() },
                                    modifier = Modifier.size(40.dp), shape = CircleShape,
                                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = Purple)
                                ) {
                                    Icon(if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        stringResource(if (isPlaying) R.string.pause else R.string.play), modifier = Modifier.size(20.dp))
                                }
                                Spacer(Modifier.width(8.dp))
                                // Stop
                                IconButton(onClick = { viewModel.radioPlayer.stop() }, modifier = Modifier.size(40.dp)) {
                                    Icon(Icons.Default.Stop, stringResource(R.string.stop), tint = ErrorRed, modifier = Modifier.size(24.dp))
                                }
                                Spacer(Modifier.width(8.dp))
                                // Volume
                                Slider(
                                    value = playerState.volume, onValueChange = { viewModel.radioPlayer.setVolume(it) },
                                    modifier = Modifier.weight(1f), valueRange = 0f..2f,
                                    colors = SliderDefaults.colors(thumbColor = Purple, activeTrackColor = Purple, inactiveTrackColor = GlassBorder)
                                )
                                Spacer(Modifier.width(4.dp))
                                // Slaaptimer
                                IconButton(onClick = { viewModel.setShowSleepTimer(true) }, modifier = Modifier.size(36.dp)) {
                                    Icon(Icons.Filled.Bedtime, stringResource(R.string.sleep_timer), tint = Cyan, modifier = Modifier.size(20.dp))
                                }
                                // Full player
                                IconButton(onClick = onOpenFullPlayer, modifier = Modifier.size(36.dp)) {
                                    Icon(Icons.Default.OpenInFull, stringResource(R.string.full_player), tint = SecondaryText, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }

            // Featured
            if (featured.isNotEmpty()) {
                item { SectionTitle("⭐ ${stringResource(R.string.featured_stations)}") }
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(featured, key = { it.id }) { station ->
                            StationCard(name = station.name, logoUrl = station.logoUrl, description = station.description,
                                isLive = true, onClick = { viewModel.playStation(station) })
                        }
                    }
                }
            }

            item { SectionTitle("📻 ${stringResource(R.string.all_stations)}") }
            if (stations.isEmpty()) {
                item { Box(Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.loading), color = SecondaryText) } }
            }
            items(stations, key = { it.id }) { station ->
                StationListItem(name = station.name, logoUrl = station.logoUrl, description = station.description,
                    onClick = { viewModel.playStation(station) })
            }
            item { Spacer(Modifier.height(80.dp)) }
        }
    }

    if (showSleepTimer) {
        SleepTimerDialogFull(
            sleepTimerManager = viewModel.radioPlayer.sleepTimerManager,
            onDismiss = { viewModel.setShowSleepTimer(false) },
            onStop = { viewModel.radioPlayer.stop() }
        )
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
        shape = RoundedCornerShape(14.dp), color = Card) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            if (logoUrl.isNotEmpty())
                AsyncImage(model = logoUrl, contentDescription = null, modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp)), contentScale = ContentScale.Crop)
            else PlaceholderLogo(name, 44)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(name, color = PrimaryText, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                if (description.isNotEmpty()) Text(description, color = SecondaryText, fontSize = 11.sp, maxLines = 1)
            }
            Icon(Icons.Default.PlayArrow, stringResource(R.string.play), tint = Purple, modifier = Modifier.size(22.dp))
        }
    }
}
