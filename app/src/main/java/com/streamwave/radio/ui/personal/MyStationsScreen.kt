package com.streamwave.radio.ui.personal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.streamwave.radio.R
import com.streamwave.radio.core.theme.*
import com.streamwave.radio.data.database.entity.PersonalStationEntity
import com.streamwave.radio.ui.components.PlaceholderLogo
import com.streamwave.radio.ui.home.HomeViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyStationsScreen(onBack: () -> Unit, homeViewModel: HomeViewModel = hiltViewModel()) {
    val dao = remember {
        val app = androidx.compose.ui.platform.LocalContext.current.applicationContext as com.streamwave.radio.StreamWaveApp
        app.db.personalStationDao()
    }
    var stations by remember { mutableStateOf<List<PersonalStationEntity>>(emptyList()) }

    LaunchedEffect(Unit) {
        dao.getAll().collect { stations = it }
    }

    Scaffold(containerColor = Background,
        topBar = { TopAppBar(title = { Text(stringResource(R.string.my_stations), color = PrimaryText) },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back), tint = PrimaryText) } },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)) }
    ) { padding ->
        if (stations.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Radio, null, tint = Purple, modifier = Modifier.size(48.dp))
                    Spacer(Modifier.height(16.dp))
                    Text(stringResource(R.string.no_stations), color = SecondaryText)
                    Text("Voeg een persoonlijk station toe via de + knop", color = SecondaryText, fontSize = 12.sp)
                }
            }
        } else {
            LazyColumn(Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                item { Spacer(Modifier.height(4.dp)) }
                items(stations, key = { it.id }) { station ->
                    Surface(
                        onClick = { homeViewModel.radioPlayer.play(station.streamUrl, station.name) },
                        modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(14.dp), ambientColor = PurpleGlow),
                        shape = RoundedCornerShape(14.dp), color = Card
                    ) {
                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            PlaceholderLogo(station.name, 44)
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(station.name, color = PrimaryText, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                                    Spacer(Modifier.width(6.dp))
                                    Surface(color = DarkPurple, shape = RoundedCornerShape(4.dp)) {
                                        Text("Persoonlijk", color = PrimaryText, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp))
                                    }
                                }
                                if (station.description.isNotEmpty()) Text(station.description, color = SecondaryText, fontSize = 11.sp, maxLines = 1)
                            }
                            Icon(Icons.Default.PlayArrow, stringResource(R.string.play), tint = Purple, modifier = Modifier.size(22.dp))
                        }
                    }
                }
            }
        }
    }
}
