package com.streamwave.radio.data.repository

import com.streamwave.radio.data.database.entity.RecentStationEntity
import kotlinx.coroutines.flow.Flow

interface RecentStationRepository {
    fun getRecent(limit: Int): Flow<List<RecentStationEntity>>
    suspend fun addRecent(type: String, stationId: Long)
    suspend fun deleteAll()
}
