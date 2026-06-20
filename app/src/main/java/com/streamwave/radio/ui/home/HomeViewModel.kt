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
    private val personalStationDao: com.streamwave.radio.data.database.dao.PersonalStationDao,
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

    private val _showAd = MutableStateFlow(false)
    val showAd: StateFlow<Boolean> = _showAd.asStateFlow()

    private val _currentStationId = MutableStateFlow<Long?>(null)
    val currentStationId: StateFlow<Long?> = _currentStationId.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    fun setShowSleepTimer(show: Boolean) { _showSleepTimer.value = show }
    fun dismissAd() { _showAd.value = false }
    fun personalStationDao() = personalStationDao

    init {
        viewModelScope.launch { stationRepository.getOfficialStations().collect { _officialStations.value = it } }
        viewModelScope.launch { stationRepository.getFeaturedStations().collect { _featuredStations.value = it } }
        // Toon reclame na 60 seconden
        viewModelScope.launch {
            kotlinx.coroutines.delay(60_000L)
            _showAd.value = true
        }
    }

    fun onSearch(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            if (query.isBlank()) stationRepository.getOfficialStations().collect { _officialStations.value = it }
            else stationRepository.searchStations(query).collect { _officialStations.value = it }
        }
    }

    fun playStation(station: OfficialStationEntity) {
        viewModelScope.launch { recentStationRepository.addRecent("OFFICIAL", station.id) }
        _currentStationId.value = station.id
        // Check of dit station favoriet is
        viewModelScope.launch { _isFavorite.value = favoriteRepository.isFavorite("OFFICIAL", station.id) }
        radioPlayer.play(station.streamUrl, station.name, station.logoUrl)
    }

    fun toggleFavorite() {
        val stationId = _currentStationId.value ?: return
        viewModelScope.launch {
            val isNowFav = favoriteRepository.toggle("OFFICIAL", stationId)
            _isFavorite.value = isNowFav
        }
    }

    fun getFavorites(): StateFlow<List<Long>> {
        val ids = MutableStateFlow<List<Long>>(emptyList())
        viewModelScope.launch {
            favoriteRepository.getAll().collect { favs ->
                ids.value = favs.filter { it.stationType == "OFFICIAL" }.map { it.stationId }
            }
        }
        return ids.asStateFlow()
    }
}
