package com.streamwave.radio.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.streamwave.radio.data.database.entity.OfficialStationEntity
import com.streamwave.radio.data.repository.FavoriteRepository
import com.streamwave.radio.data.repository.RecentStationRepository
import com.streamwave.radio.data.repository.StationRepository
import com.streamwave.radio.player.RadioPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _officialStations = MutableStateFlow<List<OfficialStationEntity>>(emptyList())
    val officialStations: StateFlow<List<OfficialStationEntity>> = _officialStations.asStateFlow()

    private val _featuredStations = MutableStateFlow<List<OfficialStationEntity>>(emptyList())
    val featuredStations: StateFlow<List<OfficialStationEntity>> = _featuredStations.asStateFlow()

    private val _showSleepTimer = MutableStateFlow(false)
    val showSleepTimer: StateFlow<Boolean> = _showSleepTimer.asStateFlow()

    fun setShowSleepTimer(show: Boolean) { _showSleepTimer.value = show }

    init {
        // Laad stations direct bij opstarten
        viewModelScope.launch {
            stationRepository.getOfficialStations().collect { stations ->
                _officialStations.value = stations
            }
        }
        viewModelScope.launch {
            stationRepository.getFeaturedStations().collect { featured ->
                _featuredStations.value = featured
            }
        }
    }

    fun onSearch(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            viewModelScope.launch {
                stationRepository.getOfficialStations().collect { stations ->
                    _officialStations.value = stations
                }
            }
        } else {
            viewModelScope.launch {
                stationRepository.searchStations(query).collect { stations ->
                    _officialStations.value = stations
                }
            }
        }
    }

    fun playStation(station: OfficialStationEntity) {
        viewModelScope.launch {
            recentStationRepository.addRecent("OFFICIAL", station.id)
        }
        radioPlayer.play(station.streamUrl, station.name, station.logoUrl)
    }
}
