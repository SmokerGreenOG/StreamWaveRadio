package com.streamwave.radio.ui.favorites

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
import com.streamwave.radio.data.database.entity.FavoriteEntity
import com.streamwave.radio.data.repository.FavoriteRepository
import com.streamwave.radio.data.repository.StationRepository
import com.streamwave.radio.ui.components.PlaceholderLogo
import com.streamwave.radio.ui.home.HomeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onBack: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    // Haal favorites op via viewModel
    val favoriteIds by homeViewModel.getFavorites().collectAsState()
    val allStations by homeViewModel.officialStations.collectAsState()
    val favStations = allStations.filter { it.id in favoriteIds }

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.favorites), color = PrimaryText) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back), tint = PrimaryText) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        }
    ) { padding ->
        if (favStations.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.FavoriteBorder, null, tint = Pink, modifier = Modifier.size(48.dp))
                    Spacer(Modifier.height(16.dp))
                    Text(stringResource(R.string.no_favorites), color = SecondaryText)
                }
            }
        } else {
            LazyColumn(Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                item { Spacer(Modifier.height(4.dp)) }
                items(favStations, key = { it.id }) { station ->
                    Surface(
                        onClick = { homeViewModel.playStation(station) },
                        modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(14.dp), ambientColor = PurpleGlow),
                        shape = RoundedCornerShape(14.dp), color = Card
                    ) {
                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            if (station.logoUrl.isNotEmpty())
                                AsyncImage(model = station.logoUrl, contentDescription = null, modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp)), contentScale = ContentScale.Crop)
                            else PlaceholderLogo(station.name, 44)
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(station.name, color = PrimaryText, fontWeight = FontWeight.Medium, fontSize = 14.sp)
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
