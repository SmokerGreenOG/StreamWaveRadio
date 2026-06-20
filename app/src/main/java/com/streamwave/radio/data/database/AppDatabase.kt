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
import kotlinx.coroutines.Dispatchers

@Database(
    entities = [
        OfficialStationEntity::class, PersonalStationEntity::class, CategoryEntity::class,
        FavoriteEntity::class, RecentStationEntity::class, StationSubmissionEntity::class
    ],
    version = 4, exportSchema = false
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

class PrepopulateCallback : androidx.room.RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        kotlinx.coroutines.runBlocking {
            kotlinx.coroutines.withContext(Dispatchers.IO) {
                try { prepopulateDatabase(db) } catch (_: Exception) {}
            }
        }
    }

    private fun prepopulateDatabase(db: SupportSQLiteDatabase) {
        val categories = listOf(
            CategoryEntity(1,"Pop","Pop","Pop","Pop",1), CategoryEntity(2,"Rock","Rock","Rock","Rock",2),
            CategoryEntity(3,"Dance","Dance","Dance","Dance",3), CategoryEntity(4,"Hip-hop","Hip-hop","Hip-hop","Hip-hop",4),
            CategoryEntity(5,"Reggae","Reggae","Reggae","Reggae",5), CategoryEntity(6,"Nederlandstalig","Nederlandstalig","Niederländisch","Holandés",6),
            CategoryEntity(7,"Classics","Classics","Klassiker","Clásicos",7), CategoryEntity(8,"Jazz","Jazz","Jazz","Jazz",8),
            CategoryEntity(9,"Nieuws","News","Nachrichten","Noticias",9), CategoryEntity(10,"Talk","Talk","Gespräch","Conversación",10),
            CategoryEntity(11,"Hardcore","Hardcore","Hardcore","Hardcore",11), CategoryEntity(12,"Chill","Chill","Chill","Relajado",12),
            CategoryEntity(13,"Internetradio","Internet Radio","Internetradio","Radio Internet",13),
            CategoryEntity(14,"Lokaal","Local","Lokal","Local",14), CategoryEntity(15,"Overig","Other","Sonstige","Otro",15)
        )
        categories.forEach { c -> db.execSQL("INSERT OR IGNORE INTO categories (id,name,nameNl,nameDe,nameEs,sortOrder) VALUES (?,?,?,?,?,?)", arrayOf(c.id,c.name,c.nameNl,c.nameDe,c.nameEs,c.sortOrder)) }

        val n = System.currentTimeMillis()
        val s = listOf(
            OfficialStationEntity(1,"Qmusic Foute Uur","https://icecast-qmusicnl-cdp.triple-it.nl/Qmusic_nl_fouteuur_96.mp3","https://www.qmusic.nl","https://static.mytuner.mobi/media/tvos_radios/758/qmusic-foute-uur.809d5438.png",1,"Nederland","Nederlands","Non-stop guilty pleasures en foute hits!",true,true,1,"ONLINE",n),
            OfficialStationEntity(2,"Qmusic Live","https://icecast-qmusicnl-cdp.triple-it.nl/Qmusic_nl_live_96.mp3","https://www.qmusic.nl","",1,"Nederland","Nederlands","Qmusic — hits van nu en classics live!",true,true,2,"ONLINE",n),
            OfficialStationEntity(3,"Radio 538","https://playerservices.streamtheworld.com/api/livestream-redirect/RADIO538.mp3","https://www.538.nl","",1,"Nederland","Nederlands","Radio 538 — altijd de beste hits!",true,true,3,"ONLINE",n),
            OfficialStationEntity(4,"Radio 538 Dance Dept","https://playerservices.streamtheworld.com/api/livestream-redirect/TLPSTR08.mp3","https://www.538.nl","",3,"Nederland","Nederlands","538 Dance Department — de beste dance!",true,false,4,"ONLINE",n),
            OfficialStationEntity(5,"Radio 538 Non-Stop","https://playerservices.streamtheworld.com/api/livestream-redirect/TLPSTR09.mp3","https://www.538.nl","",1,"Nederland","Nederlands","538 Non-Stop — onafgebroken hits!",true,false,5,"ONLINE",n),
            OfficialStationEntity(6,"538 80s Hits","https://playerservices.streamtheworld.com/api/livestream-redirect/TLPSTR15.mp3","https://www.538.nl","",7,"Nederland","Nederlands","538 80s — de grootste 80s hits non-stop!",true,true,6,"ONLINE",n),
            OfficialStationEntity(7,"Radio 10","https://playerservices.streamtheworld.com/api/livestream-redirect/RADIO10.mp3","https://www.radio10.nl","",7,"Nederland","Nederlands","Radio 10 — de grootste hits aller tijden!",true,false,7,"ONLINE",n),
            OfficialStationEntity(8,"Radio 10 80s Hits","https://playerservices.streamtheworld.com/api/livestream-redirect/TLPSTR20.mp3","https://www.radio10.nl","",7,"Nederland","Nederlands","Radio 10 — de beste 80s hits!",true,false,8,"ONLINE",n),
            OfficialStationEntity(9,"Radio 10 90s Hits","https://playerservices.streamtheworld.com/api/livestream-redirect/TLPSTR04.mp3","https://www.radio10.nl","",7,"Nederland","Nederlands","Radio 10 — de beste 90s hits!",true,false,9,"ONLINE",n),
            OfficialStationEntity(10,"Radio 10 Love Songs","https://playerservices.streamtheworld.com/api/livestream-redirect/TLPSTR18.mp3","https://www.radio10.nl","",7,"Nederland","Nederlands","Radio 10 — de mooiste love songs!",true,false,10,"ONLINE",n),
            OfficialStationEntity(11,"Radio Veronica","https://playerservices.streamtheworld.com/api/livestream-redirect/VERONICA.mp3","https://www.radioveronica.nl","",2,"Nederland","Nederlands","Radio Veronica — de beste rock en pop!",true,false,11,"ONLINE",n),
            OfficialStationEntity(12,"Radio Veronica Rock","https://playerservices.streamtheworld.com/api/livestream-redirect/SRGSTR11.mp3","https://www.radioveronica.nl","",2,"Nederland","Nederlands","Veronica Rock — non-stop rock classics!",true,false,12,"ONLINE",n),
            OfficialStationEntity(13,"Radio Veronica Top 1000","https://playerservices.streamtheworld.com/api/livestream-redirect/SRGSTR09.mp3","https://www.radioveronica.nl","",7,"Nederland","Nederlands","Veronica Top 1000 Allertijden!",true,false,13,"ONLINE",n),
            OfficialStationEntity(14,"Sky Radio","https://playerservices.streamtheworld.com/api/livestream-redirect/SKYRADIO.mp3","https://www.skyradio.nl","",12,"Nederland","Nederlands","Sky Radio — Smooth & Relaxed!",true,true,14,"ONLINE",n),
            OfficialStationEntity(15,"Sky Radio Hits","https://playerservices.streamtheworld.com/api/livestream-redirect/SRGSTR01.mp3","https://www.skyradio.nl","",1,"Nederland","Nederlands","Sky Radio Hits — de grootste hits!",true,false,15,"ONLINE",n),
            OfficialStationEntity(16,"Sky Radio Lounge","https://playerservices.streamtheworld.com/api/livestream-redirect/SRGSTR07.mp3","https://www.skyradio.nl","",12,"Nederland","Nederlands","Sky Radio Lounge — heerlijk relaxed!",true,false,16,"ONLINE",n),
            OfficialStationEntity(17,"Slam! FM","https://playerservices.streamtheworld.com/api/livestream-redirect/SLAM_MP3.mp3","https://www.slam.nl","",3,"Nederland","Nederlands","Slam! — de beste dance, hip-hop en hits!",true,true,17,"ONLINE",n),
            OfficialStationEntity(18,"Slam! Non-Stop","https://playerservices.streamtheworld.com/api/livestream-redirect/WEB15_MP3.mp3","https://www.slam.nl","",3,"Nederland","Nederlands","Slam! Non-Stop — onafgebroken beats!",true,false,18,"ONLINE",n),
            OfficialStationEntity(19,"BNR Nieuwsradio","https://playerservices.streamtheworld.com/api/livestream-redirect/BNR_NIEUWSRADIO.mp3","https://www.bnr.nl","",9,"Nederland","Nederlands","BNR — zakelijk nieuws en interviews",true,false,19,"ONLINE",n),
            OfficialStationEntity(20,"Sublime FM","https://playerservices.streamtheworld.com/api/livestream-redirect/SUBLIME.mp3","https://www.sublime.nl","",8,"Nederland","Nederlands","Sublime — jazz, soul & funk",true,false,20,"ONLINE",n),
            OfficialStationEntity(21,"NPO Radio 1","https://icecast.omroep.nl/radio1-bb-mp3","https://www.nporadio1.nl","",9,"Nederland","Nederlands","NPO Radio 1 — nieuws, sport en actualiteiten",true,true,21,"ONLINE",n),
            OfficialStationEntity(22,"NPO Radio 2","https://icecast.omroep.nl/radio2-bb-mp3","https://www.nporadio2.nl","",7,"Nederland","Nederlands","NPO Radio 2 — de beste muziek, de grootste hits!",true,true,22,"ONLINE",n),
            OfficialStationEntity(23,"NPO 3FM","https://icecast.omroep.nl/3fm-bb-mp3","https://www.npo3fm.nl","",1,"Nederland","Nederlands","NPO 3FM — pop, rock en alternative",true,false,23,"ONLINE",n),
            OfficialStationEntity(24,"NPO Radio 4","https://icecast.omroep.nl/radio4-bb-mp3","https://www.nporadio4.nl","",8,"Nederland","Nederlands","NPO Radio 4 — klassieke muziek",true,false,24,"ONLINE",n),
            OfficialStationEntity(25,"NPO Radio 5","https://icecast.omroep.nl/radio5-bb-mp3","https://www.nporadio5.nl","",7,"Nederland","Nederlands","NPO Radio 5 — muziek van toen en nu",true,false,25,"ONLINE",n),
            OfficialStationEntity(26,"NPO FunX","https://icecast.omroep.nl/funx-bb-mp3","https://www.funx.nl","",4,"Nederland","Nederlands","FunX — hip-hop, R&B en urban",true,false,26,"ONLINE",n),
            OfficialStationEntity(27,"Kink FM","https://playerservices.streamtheworld.com/api/livestream-redirect/KINK.mp3","https://www.kink.nl","",2,"Nederland","Nederlands","Kink — de beste rock en alternative!",true,false,27,"ONLINE",n),
            OfficialStationEntity(28,"StuBru","https://icecast.vrtcdn.be/stubru-high.mp3","https://www.stubru.be","",2,"België","Nederlands","Studio Brussel — music is life!",true,true,28,"ONLINE",n),
            OfficialStationEntity(29,"MNM","https://icecast.vrtcdn.be/mnm-high.mp3","https://www.mnm.be","",1,"België","Nederlands","MNM — de beste pop en dance uit Vlaanderen!",true,false,29,"ONLINE",n),
            OfficialStationEntity(30,"Radio 1 (BE)","https://icecast.vrtcdn.be/radio1-high.mp3","https://www.radio1.be","",9,"België","Nederlands","Radio 1 — nieuws en duiding uit Vlaanderen",true,false,30,"ONLINE",n),
            OfficialStationEntity(31,"Joe FM (BE)","https://playerservices.streamtheworld.com/api/livestream-redirect/JOE.mp3","https://www.joe.be","",7,"België","Nederlands","Joe — de grootste hits uit de 70s, 80s & 90s!",true,false,31,"ONLINE",n),
            OfficialStationEntity(99,"FreeMinds FM","","","",13,"Nederland","Nederlands","Het officiële FreeMinds radiostation — in te vullen door beheerder",false,false,99,"UNKNOWN",0),
        )
        s.forEach { st -> db.execSQL("INSERT OR IGNORE INTO official_stations (id,name,streamUrl,websiteUrl,logoUrl,categoryId,country,language,description,isActive,isFeatured,sortOrder,streamStatus,lastCheckedAt,createdAt,updatedAt) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", arrayOf(st.id,st.name,st.streamUrl,st.websiteUrl,st.logoUrl,st.categoryId,st.country,st.language,st.description,st.isActive,st.isFeatured,st.sortOrder,st.streamStatus,st.lastCheckedAt,st.createdAt,st.updatedAt)) }
    }
}
