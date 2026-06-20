package com.streamwave.radio.data.repository

import com.streamwave.radio.data.database.dao.PersonalStationDao
import com.streamwave.radio.data.database.entity.PersonalStationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonalStationRepositoryImpl @Inject constructor(
    private val personalStationDao: PersonalStationDao
) : PersonalStationRepository {

    override fun getAll(): Flow<List<PersonalStationEntity>> =
        personalStationDao.getAll()

    override fun getByUserId(userId: String): Flow<List<PersonalStationEntity>> =
        personalStationDao.getByUserId(userId)

    override fun getFavorites(): Flow<List<PersonalStationEntity>> =
        personalStationDao.getFavorites()

    override fun search(query: String): Flow<List<PersonalStationEntity>> =
        personalStationDao.search(query)

    override suspend fun getById(id: Long): PersonalStationEntity? =
        personalStationDao.getById(id)

    override suspend fun insert(station: PersonalStationEntity): Long =
        personalStationDao.insert(station)

    override suspend fun update(station: PersonalStationEntity) =
        personalStationDao.update(station)

    override suspend fun delete(id: Long) =
        personalStationDao.delete(id)

    override suspend fun setFavorite(id: Long, favorite: Boolean) =
        personalStationDao.setFavorite(id, favorite)

    override suspend fun setSubmissionStatus(id: Long, status: String) =
        personalStationDao.setSubmissionStatus(id, status)

    override suspend fun updateLastPlayed(id: Long, timestamp: Long) =
        personalStationDao.updateLastPlayed(id, timestamp)
}
