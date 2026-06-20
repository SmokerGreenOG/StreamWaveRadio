package com.streamwave.radio.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "station_submissions")
data class StationSubmissionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val personalStationId: Long,
    val submittedBy: String = "",
    val name: String,
    val streamUrl: String,
    val websiteUrl: String = "",
    val category: String = "",
    val description: String = "",
    val status: String = "PENDING",
    val adminNote: String = "",
    val submittedAt: Long = System.currentTimeMillis(),
    val reviewedAt: Long = 0
)
