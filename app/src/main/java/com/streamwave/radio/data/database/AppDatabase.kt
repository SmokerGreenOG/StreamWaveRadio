package com.streamwave.radio.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.streamwave.radio.data.database.converter.Converters
import com.streamwave.radio.data.database.dao.*
import com.streamwave.radio.data.database.entity.*

@Database(
    entities = [
        OfficialStationEntity::class, PersonalStationEntity::class, CategoryEntity::class,
        FavoriteEntity::class, RecentStationEntity::class, StationSubmissionEntity::class
    ],
    version = 6, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun officialStationDao(): OfficialStationDao
    abstract fun personalStationDao(): PersonalStationDao
    abstract fun categoryDao(): CategoryDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun recentStationDao(): RecentStationDao
    abstract fun stationSubmissionDao(): StationSubmissionDao
    companion object { const val DATABASE_NAME = "streamwave_radio.db" }
}
