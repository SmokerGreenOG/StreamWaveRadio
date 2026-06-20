package com.streamwave.radio.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.streamwave.radio.data.database.entity.OfficialStationEntity
import com.streamwave.radio.data.repository.FavoriteRepository
import com.streamwave.radio.data.repository.RecentStationRepository
import com.streamwave.radio.data.repository.StationRepository
import com.streamwave.radio.player.RadioPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val stationRepository: StationRepository,
    private val favoriteRepository: FavoriteRepository,
    private val recentStationRepository: RecentStationRepository,
    val radioPlayer: RadioPlayer
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val officialStations: StateFlow<List<OfficialStationEntity>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) stationRepository.getOfficialStations()
            else stationRepository.searchStations(query)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val featuredStations: StateFlow<List<OfficialStationEntity>> =
        stationRepository.getFeaturedStations()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val recentStations = recentStationRepository.getRecent(10)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun onSearch(query: String) { _searchQuery.value = query }

    fun playStation(station: OfficialStationEntity) {
        viewModelScope.launch {
            recentStationRepository.addRecent("OFFICIAL", station.id)
        }
        radioPlayer.play(station.streamUrl, station.name, station.logoUrl)
    }
}
