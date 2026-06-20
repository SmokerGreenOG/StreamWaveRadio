package com.streamwave.radio.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.streamwave.radio.data.database.converter.Converters
import com.streamwave.radio.data.database.dao.CategoryDao
import com.streamwave.radio.data.database.dao.FavoriteDao
import com.streamwave.radio.data.database.dao.OfficialStationDao
import com.streamwave.radio.data.database.dao.PersonalStationDao
import com.streamwave.radio.data.database.dao.RecentStationDao
import com.streamwave.radio.data.database.dao.StationSubmissionDao
import com.streamwave.radio.data.database.entity.CategoryEntity
import com.streamwave.radio.data.database.entity.FavoriteEntity
import com.streamwave.radio.data.database.entity.OfficialStationEntity
import com.streamwave.radio.data.database.entity.PersonalStationEntity
import com.streamwave.radio.data.database.entity.RecentStationEntity
import com.streamwave.radio.data.database.entity.StationSubmissionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        OfficialStationEntity::class,
        PersonalStationEntity::class,
        CategoryEntity::class,
        FavoriteEntity::class,
        RecentStationEntity::class,
        StationSubmissionEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun officialStationDao(): OfficialStationDao
    abstract fun personalStationDao(): PersonalStationDao
    abstract fun categoryDao(): CategoryDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun recentStationDao(): RecentStationDao
    abstract fun stationSubmissionDao(): StationSubmissionDao

    companion object {
        const val DATABASE_NAME = "streamwave_radio.db"
    }
}

class PrepopulateCallback : androidx.room.RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        // Synchrone prepopulatie via runBlocking — data is direct beschikbaar
        kotlinx.coroutines.runBlocking {
            with(kotlinx.coroutines.Dispatchers.IO) {
                kotlinx.coroutines.withContext(this) {
                    try { prepopulateDatabase(db) } catch (_: Exception) {}
                }
            }
        }
    }

    private fun prepopulateDatabase(db: SupportSQLiteDatabase) {
        // Categories
        val categories = listOf(
            CategoryEntity(1, "Pop", "Pop", "Pop", "Pop", 1),
            CategoryEntity(2, "Rock", "Rock", "Rock", "Rock", 2),
            CategoryEntity(3, "Dance", "Dance", "Dance", "Dance", 3),
            CategoryEntity(4, "Hip-hop", "Hip-hop", "Hip-hop", "Hip-hop", 4),
            CategoryEntity(5, "Reggae", "Reggae", "Reggae", "Reggae", 5),
            CategoryEntity(6, "Nederlandstalig", "Nederlandstalig", "Niederländisch", "Holandés", 6),
            CategoryEntity(7, "Classics", "Classics", "Klassiker", "Clásicos", 7),
            CategoryEntity(8, "Jazz", "Jazz", "Jazz", "Jazz", 8),
            CategoryEntity(9, "Nieuws", "News", "Nachrichten", "Noticias", 9),
            CategoryEntity(10, "Talk", "Talk", "Gespräch", "Conversación", 10),
            CategoryEntity(11, "Hardcore", "Hardcore", "Hardcore", "Hardcore", 11),
            CategoryEntity(12, "Chill", "Chill", "Chill", "Relajado", 12),
            CategoryEntity(13, "Internetradio", "Internet Radio", "Internetradio", "Radio Internet", 13),
            CategoryEntity(14, "Lokaal", "Local", "Lokal", "Local", 14),
            CategoryEntity(15, "Overig", "Other", "Sonstige", "Otro", 15)
        )

        categories.forEach { category ->
            db.execSQL(
                """INSERT OR IGNORE INTO categories (id, name, nameNl, nameDe, nameEs, sortOrder) 
                   VALUES (?, ?, ?, ?, ?, ?)""",
                arrayOf(category.id, category.name, category.nameNl, category.nameDe, category.nameEs, category.sortOrder)
            )
        }

        // Demo official stations — werkende streams, getest 20-06-2026
        val now = System.currentTimeMillis()
        val demoStations = listOf(
            OfficialStationEntity(
                id = 1, name = "Qmusic Foute Uur",
                streamUrl = "https://icecast-qmusicnl-cdp.triple-it.nl/Qmusic_nl_fouteuur_96.mp3",
                websiteUrl = "https://www.qmusic.nl", logoUrl = "https://static.mytuner.mobi/media/tvos_radios/758/qmusic-foute-uur.809d5438.png",
                categoryId = 1, country = "Nederland", language = "Nederlands",
                description = "Non-stop de leukste guilty pleasures en foute hits! 24/7 Qmusic Foute Uur.",
                isActive = true, isFeatured = true, sortOrder = 1,
                streamStatus = "ONLINE", lastCheckedAt = now
            ),
            OfficialStationEntity(
                id = 2, name = "Qmusic Live",
                streamUrl = "https://icecast-qmusicnl-cdp.triple-it.nl/Qmusic_nl_live_96.mp3",
                websiteUrl = "https://www.qmusic.nl", logoUrl = "https://static.mytuner.mobi/media/tvos_radios/758/qmusic-foute-uur.809d5438.png",
                categoryId = 1, country = "Nederland", language = "Nederlands",
                description = "Qmusic Nederland — de hits van nu en de beste classics live!",
                isActive = true, isFeatured = true, sortOrder = 2,
                streamStatus = "ONLINE", lastCheckedAt = now
            ),
            OfficialStationEntity(
                id = 3, name = "Radio 538",
                streamUrl = "https://playerservices.streamtheworld.com/api/livestream-redirect/RADIO538.mp3",
                websiteUrl = "https://www.538.nl", logoUrl = "",
                categoryId = 1, country = "Nederland", language = "Nederlands",
                description = "Radio 538 — altijd de beste hits, live vanuit Hilversum!",
                isActive = true, isFeatured = false, sortOrder = 3,
                streamStatus = "ONLINE", lastCheckedAt = now
            ),
            OfficialStationEntity(
                id = 4, name = "FreeMinds FM",
                streamUrl = "",
                websiteUrl = "", logoUrl = "",
                categoryId = 13, country = "Nederland", language = "Nederlands",
                description = "Het officiële FreeMinds radiostation — stream URL in te vullen door beheerder",
                isActive = false, isFeatured = false, sortOrder = 10
            )
        )

        demoStations.forEach { station ->
            db.execSQL(
                """INSERT OR IGNORE INTO official_stations 
                   (id, name, streamUrl, websiteUrl, logoUrl, categoryId, country, language, description, isActive, isFeatured, sortOrder, streamStatus, lastCheckedAt, createdAt, updatedAt)
                   VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)""",
                arrayOf(
                    station.id, station.name, station.streamUrl, station.websiteUrl,
                    station.logoUrl, station.categoryId, station.country, station.language,
                    station.description, station.isActive, station.isFeatured, station.sortOrder,
                    station.streamStatus, station.lastCheckedAt, station.createdAt, station.updatedAt
                )
            )
        }
    }
}
