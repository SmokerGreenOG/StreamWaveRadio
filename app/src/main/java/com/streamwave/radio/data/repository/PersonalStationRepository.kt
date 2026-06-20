package com.streamwave.radio.data.repository

import com.streamwave.radio.data.database.entity.PersonalStationEntity
import kotlinx.coroutines.flow.Flow

interface PersonalStationRepository {
    fun getAll(): Flow<List<PersonalStationEntity>>
    fun getByUserId(userId: String): Flow<List<PersonalStationEntity>>
    fun getFavorites(): Flow<List<PersonalStationEntity>>
    fun search(query: String): Flow<List<PersonalStationEntity>>
    suspend fun getById(id: Long): PersonalStationEntity?
    suspend fun insert(station: PersonalStationEntity): Long
    suspend fun update(station: PersonalStationEntity)
    suspend fun delete(id: Long)
    suspend fun setFavorite(id: Long, favorite: Boolean)
    suspend fun setSubmissionStatus(id: Long, status: String)
    suspend fun updateLastPlayed(id: Long, timestamp: Long)
}
