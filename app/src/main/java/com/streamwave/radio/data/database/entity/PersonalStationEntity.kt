package com.streamwave.radio.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personal_stations")
data class PersonalStationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: String? = null,
    val name: String,
    val streamUrl: String,
    val websiteUrl: String = "",
    val localLogoPath: String = "",
    val categoryId: Long = 0,
    val description: String = "",
    val isFavorite: Boolean = false,
    val submissionStatus: String = "NOT_SUBMITTED",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val lastPlayedAt: Long = 0
)
