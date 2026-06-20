package com.streamwave.radio.data.repository

import com.streamwave.radio.data.database.dao.RecentStationDao
import com.streamwave.radio.data.database.entity.RecentStationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecentStationRepositoryImpl @Inject constructor(
    private val recentStationDao: RecentStationDao
) : RecentStationRepository {

    override fun getRecent(limit: Int): Flow<List<RecentStationEntity>> =
        recentStationDao.getRecent(limit)

    override suspend fun addRecent(type: String, stationId: Long) {
        recentStationDao.insert(
            RecentStationEntity(
                stationType = type,
                stationId = stationId,
                playedAt = System.currentTimeMillis()
            )
        )
        // Houd max 100 recente stations
        recentStationDao.deleteOldest(50)
    }

    override suspend fun deleteAll() =
        recentStationDao.deleteAll()
}
