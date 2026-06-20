package com.streamwave.radio.data.repository

import com.streamwave.radio.data.database.dao.OfficialStationDao
import com.streamwave.radio.data.database.entity.OfficialStationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StationRepositoryImpl @Inject constructor(
    private val officialStationDao: OfficialStationDao
) : StationRepository {

    override fun getOfficialStations(): Flow<List<OfficialStationEntity>> =
        officialStationDao.getAllActive()

    override fun getFeaturedStations(): Flow<List<OfficialStationEntity>> =
        officialStationDao.getFeatured()

    override fun getStationsByCategory(categoryId: Long): Flow<List<OfficialStationEntity>> =
        officialStationDao.getByCategory(categoryId)

    override fun searchStations(query: String): Flow<List<OfficialStationEntity>> =
        officialStationDao.search(query)

    override suspend fun getStationById(id: Long): OfficialStationEntity? =
        officialStationDao.getById(id)

    override suspend fun insertOfficial(station: OfficialStationEntity): Long =
        officialStationDao.insert(station)

    override suspend fun updateOfficial(station: OfficialStationEntity) =
        officialStationDao.update(station)

    override suspend fun deleteOfficial(id: Long) =
        officialStationDao.delete(id)

    override suspend fun setOfficialActive(id: Long, active: Boolean) =
        officialStationDao.setActive(id, active)

    override suspend fun updateStreamStatus(id: Long, status: String, checkedAt: Long) =
        officialStationDao.updateStreamStatus(id, status, checkedAt)
}
