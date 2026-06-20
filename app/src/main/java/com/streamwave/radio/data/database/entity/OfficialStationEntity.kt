package com.streamwave.radio.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "official_stations")
data class OfficialStationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val streamUrl: String,
    val websiteUrl: String = "",
    val logoUrl: String = "",
    val categoryId: Long = 0,
    val country: String = "",
    val language: String = "",
    val description: String = "",
    val isActive: Boolean = true,
    val isFeatured: Boolean = false,
    val sortOrder: Int = 0,
    val streamStatus: String = "UNKNOWN",
    val lastCheckedAt: Long = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
