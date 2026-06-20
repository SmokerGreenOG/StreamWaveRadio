package com.streamwave.radio.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val stationType: String, // "OFFICIAL" or "PERSONAL"
    val stationId: Long,
    val sortOrder: Int = 0,
    val addedAt: Long = System.currentTimeMillis()
)
