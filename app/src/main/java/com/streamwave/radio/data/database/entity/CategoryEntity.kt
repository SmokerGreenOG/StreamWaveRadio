package com.streamwave.radio.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val nameNl: String = "",
    val nameDe: String = "",
    val nameEs: String = "",
    val sortOrder: Int = 0
)
