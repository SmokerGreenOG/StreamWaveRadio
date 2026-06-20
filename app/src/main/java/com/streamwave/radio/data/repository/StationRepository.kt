package com.streamwave.radio.data.repository

import com.streamwave.radio.data.database.entity.OfficialStationEntity
import kotlinx.coroutines.flow.Flow

interface StationRepository {
    fun getOfficialStations(): Flow<List<OfficialStationEntity>>
    fun getFeaturedStations(): Flow<List<OfficialStationEntity>>
    fun getStationsByCategory(categoryId: Long): Flow<List<OfficialStationEntity>>
    fun searchStations(query: String): Flow<List<OfficialStationEntity>>
    suspend fun getStationById(id: Long): OfficialStationEntity?
    suspend fun insertOfficial(station: OfficialStationEntity): Long
    suspend fun updateOfficial(station: OfficialStationEntity)
    suspend fun deleteOfficial(id: Long)
    suspend fun setOfficialActive(id: Long, active: Boolean)
    suspend fun updateStreamStatus(id: Long, status: String, checkedAt: Long)
}
