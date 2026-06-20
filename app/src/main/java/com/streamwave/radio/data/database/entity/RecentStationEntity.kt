package com.streamwave.radio.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_stations")
data class RecentStationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val stationType: String, // "OFFICIAL" or "PERSONAL"
    val stationId: Long,
    val playedAt: Long = System.currentTimeMillis()
)
