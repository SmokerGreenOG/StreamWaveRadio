package com.streamwave.radio

import android.app.Application
import com.streamwave.radio.core.localization.LanguageManager
import com.streamwave.radio.data.database.AppDatabase
import com.streamwave.radio.data.database.entity.CategoryEntity
import com.streamwave.radio.data.database.entity.OfficialStationEntity
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class StreamWaveApp : Application() {

    @Inject lateinit var db: AppDatabase
    @Inject lateinit var languageManager: LanguageManager

    override fun onCreate() {
        super.onCreate()
        // Pas opgeslagen taal toe bij opstarten
        languageManager.applySavedLanguage()
        CoroutineScope(Dispatchers.IO).launch {
            prepopulateIfNeeded()
        }
    }

    private suspend fun prepopulateIfNeeded() {
        val catDao = db.categoryDao()
        val stationDao = db.officialStationDao()

        // Check of data al bestaat
        val existing = catDao.getAll().firstOrNull()
        if (existing != null && existing.isNotEmpty()) return

        // Categories
        listOf(
            CategoryEntity(1,"Pop","Pop","Pop","Pop",1), CategoryEntity(2,"Rock","Rock","Rock","Rock",2),
            CategoryEntity(3,"Dance","Dance","Dance","Dance",3), CategoryEntity(4,"Hip-hop","Hip-hop","Hip-hop","Hip-hop",4),
            CategoryEntity(5,"Reggae","Reggae","Reggae","Reggae",5), CategoryEntity(6,"Nederlandstalig","Nederlandstalig","Niederländisch","Holandés",6),
            CategoryEntity(7,"Classics","Classics","Klassiker","Clásicos",7), CategoryEntity(8,"Jazz","Jazz","Jazz","Jazz",8),
            CategoryEntity(9,"Nieuws","News","Nachrichten","Noticias",9), CategoryEntity(10,"Talk","Talk","Gespräch","Conversación",10),
            CategoryEntity(11,"Hardcore","Hardcore","Hardcore","Hardcore",11), CategoryEntity(12,"Chill","Chill","Chill","Relajado",12),
            CategoryEntity(13,"Internetradio","Internet Radio","Internetradio","Radio Internet",13),
            CategoryEntity(14,"Lokaal","Local","Lokal","Local",14), CategoryEntity(15,"Overig","Other","Sonstige","Otro",15)
        ).forEach { catDao.insert(it) }

        val n = System.currentTimeMillis()
        listOf(
            // === Onze curated streams (handmatig getest) ===
            OfficialStationEntity(1,"Qmusic Foute Uur","https://icecast-qmusicnl-cdp.triple-it.nl/Qmusic_nl_fouteuur_96.mp3","https://www.qmusic.nl","https://static.mytuner.mobi/media/tvos_radios/758/qmusic-foute-uur.809d5438.png",1,"Nederland","Nederlands","Non-stop guilty pleasures en foute hits!",true,true,1,"ONLINE",n),
            OfficialStationEntity(2,"Qmusic Live","https://icecast-qmusicnl-cdp.triple-it.nl/Qmusic_nl_live_96.mp3","https://www.qmusic.nl","https://www.qmusic.nl/favicon.ico",1,"Nederland","Nederlands","Qmusic — hits van nu en classics live!",true,true,2,"ONLINE",n),
            OfficialStationEntity(3,"Radio 538","https://playerservices.streamtheworld.com/api/livestream-redirect/RADIO538.mp3","https://www.538.nl","https://www.538.nl/favicon.ico",1,"Nederland","Nederlands","Radio 538 — altijd de beste hits!",true,true,3,"ONLINE",n),
            OfficialStationEntity(4,"538 Dance Dept","https://playerservices.streamtheworld.com/api/livestream-redirect/TLPSTR08.mp3","https://www.538.nl","https://www.538.nl/favicon.ico",3,"Nederland","Nederlands","538 Dance Department — de beste dance!",true,false,4,"ONLINE",n),
            OfficialStationEntity(5,"538 Non-Stop","https://playerservices.streamtheworld.com/api/livestream-redirect/TLPSTR09.mp3","https://www.538.nl","https://www.538.nl/favicon.ico",1,"Nederland","Nederlands","538 Non-Stop — onafgebroken hits!",true,false,5,"ONLINE",n),
            OfficialStationEntity(6,"538 80s Hits","https://playerservices.streamtheworld.com/api/livestream-redirect/TLPSTR15.mp3","https://www.538.nl","https://www.538.nl/favicon.ico",7,"Nederland","Nederlands","538 80s — de grootste 80s hits!",true,true,6,"ONLINE",n),
            OfficialStationEntity(7,"Radio 10","https://playerservices.streamtheworld.com/api/livestream-redirect/RADIO10.mp3","https://www.radio10.nl","https://www.radio10.nl/favicon.ico",7,"Nederland","Nederlands","Radio 10 — de grootste hits aller tijden!",true,false,7,"ONLINE",n),
            OfficialStationEntity(8,"Radio 10 80s Hits","https://playerservices.streamtheworld.com/api/livestream-redirect/TLPSTR20.mp3","https://www.radio10.nl","https://www.radio10.nl/favicon.ico",7,"Nederland","Nederlands","Radio 10 — de beste 80s hits!",true,false,8,"ONLINE",n),
            OfficialStationEntity(9,"Radio 10 90s Hits","https://playerservices.streamtheworld.com/api/livestream-redirect/TLPSTR04.mp3","https://www.radio10.nl","https://www.radio10.nl/favicon.ico",7,"Nederland","Nederlands","Radio 10 — de beste 90s hits!",true,false,9,"ONLINE",n),
            OfficialStationEntity(10,"Radio 10 Love Songs","https://playerservices.streamtheworld.com/api/livestream-redirect/TLPSTR18.mp3","https://www.radio10.nl","https://www.radio10.nl/favicon.ico",7,"Nederland","Nederlands","Radio 10 — de mooiste love songs!",true,false,10,"ONLINE",n),
            OfficialStationEntity(11,"Radio Veronica","https://playerservices.streamtheworld.com/api/livestream-redirect/VERONICA.mp3","https://www.radioveronica.nl","https://www.radioveronica.nl/favicon.ico",2,"Nederland","Nederlands","Radio Veronica — de beste rock en pop!",true,false,11,"ONLINE",n),
            OfficialStationEntity(12,"Veronica Rock","https://playerservices.streamtheworld.com/api/livestream-redirect/SRGSTR11.mp3","https://www.radioveronica.nl","https://www.radioveronica.nl/favicon.ico",2,"Nederland","Nederlands","Veronica Rock — non-stop rock classics!",true,false,12,"ONLINE",n),
            OfficialStationEntity(13,"Veronica Top 1000","https://playerservices.streamtheworld.com/api/livestream-redirect/SRGSTR09.mp3","https://www.radioveronica.nl","https://www.radioveronica.nl/favicon.ico",7,"Nederland","Nederlands","Veronica Top 1000 Allertijden!",true,false,13,"ONLINE",n),
            OfficialStationEntity(14,"Sky Radio","https://playerservices.streamtheworld.com/api/livestream-redirect/SKYRADIO.mp3","https://www.skyradio.nl","https://www.skyradio.nl/favicon.ico",12,"Nederland","Nederlands","Sky Radio — Smooth & Relaxed!",true,true,14,"ONLINE",n),
            OfficialStationEntity(15,"Sky Radio Hits","https://playerservices.streamtheworld.com/api/livestream-redirect/SRGSTR01.mp3","https://www.skyradio.nl","https://www.skyradio.nl/favicon.ico",1,"Nederland","Nederlands","Sky Radio Hits — de grootste hits!",true,false,15,"ONLINE",n),
            OfficialStationEntity(16,"Sky Radio Lounge","https://playerservices.streamtheworld.com/api/livestream-redirect/SRGSTR07.mp3","https://www.skyradio.nl","https://www.skyradio.nl/favicon.ico",12,"Nederland","Nederlands","Sky Radio Lounge — heerlijk relaxed!",true,false,16,"ONLINE",n),
            OfficialStationEntity(17,"Slam! FM","https://playerservices.streamtheworld.com/api/livestream-redirect/SLAM_MP3.mp3","https://www.slam.nl","https://www.slam.nl/favicon.ico",3,"Nederland","Nederlands","Slam! — de beste dance en hits!",true,true,17,"ONLINE",n),
            OfficialStationEntity(18,"Slam! Non-Stop","https://playerservices.streamtheworld.com/api/livestream-redirect/WEB15_MP3.mp3","https://www.slam.nl","https://www.slam.nl/favicon.ico",3,"Nederland","Nederlands","Slam! Non-Stop — onafgebroken beats!",true,false,18,"ONLINE",n),
            OfficialStationEntity(19,"BNR Nieuwsradio","https://playerservices.streamtheworld.com/api/livestream-redirect/BNR_NIEUWSRADIO.mp3","https://www.bnr.nl","https://www.bnr.nl/favicon.ico",9,"Nederland","Nederlands","BNR — zakelijk nieuws en interviews",true,false,19,"ONLINE",n),
            OfficialStationEntity(20,"Sublime FM","https://playerservices.streamtheworld.com/api/livestream-redirect/SUBLIME.mp3","https://www.sublime.nl","https://www.sublime.nl/favicon.ico",8,"Nederland","Nederlands","Sublime — jazz, soul & funk",true,false,20,"ONLINE",n),
            OfficialStationEntity(21,"NPO Radio 1","https://icecast.omroep.nl/radio1-bb-mp3","https://www.nporadio1.nl","https://www.nporadio1.nl/favicon.ico",9,"Nederland","Nederlands","NPO Radio 1 — nieuws, sport en actualiteiten",true,true,21,"ONLINE",n),
            OfficialStationEntity(22,"NPO Radio 2","https://icecast.omroep.nl/radio2-bb-mp3","https://www.nporadio2.nl","https://www.nporadio2.nl/favicon.ico",7,"Nederland","Nederlands","NPO Radio 2 — de beste muziek!",true,true,22,"ONLINE",n),
            OfficialStationEntity(23,"NPO 3FM","https://icecast.omroep.nl/3fm-bb-mp3","https://www.npo3fm.nl","https://www.npo3fm.nl/favicon.ico",1,"Nederland","Nederlands","NPO 3FM — pop, rock en alternative",true,false,23,"ONLINE",n),
            OfficialStationEntity(24,"NPO Radio 4","https://icecast.omroep.nl/radio4-bb-mp3","https://www.nporadio4.nl","https://www.nporadio4.nl/favicon.ico",8,"Nederland","Nederlands","NPO Radio 4 — klassieke muziek",true,false,24,"ONLINE",n),
            OfficialStationEntity(25,"NPO Radio 5","https://icecast.omroep.nl/radio5-bb-mp3","https://www.nporadio5.nl","https://www.nporadio5.nl/favicon.ico",7,"Nederland","Nederlands","NPO Radio 5 — muziek van toen en nu",true,false,25,"ONLINE",n),
            OfficialStationEntity(26,"NPO FunX","https://icecast.omroep.nl/funx-bb-mp3","https://www.funx.nl","https://www.funx.nl/favicon.ico",4,"Nederland","Nederlands","FunX — hip-hop, R&B en urban",true,false,26,"ONLINE",n),
            OfficialStationEntity(27,"Kink FM","https://playerservices.streamtheworld.com/api/livestream-redirect/KINK.mp3","https://www.kink.nl","https://www.kink.nl/favicon.ico",2,"Nederland","Nederlands","Kink — de beste rock en alternative!",true,false,27,"ONLINE",n),
            OfficialStationEntity(28,"StuBru","https://icecast.vrtcdn.be/stubru-high.mp3","https://www.stubru.be","https://www.stubru.be/favicon.ico",2,"België","Nederlands","Studio Brussel — music is life!",true,true,28,"ONLINE",n),
            OfficialStationEntity(29,"MNM","https://icecast.vrtcdn.be/mnm-high.mp3","https://www.mnm.be","https://www.mnm.be/favicon.ico",1,"België","Nederlands","MNM — de beste pop en dance!",true,false,29,"ONLINE",n),
            OfficialStationEntity(30,"Radio 1 (BE)","https://icecast.vrtcdn.be/radio1-high.mp3","https://www.radio1.be","https://www.radio1.be/favicon.ico",9,"België","Nederlands","Radio 1 — nieuws en duiding uit Vlaanderen",true,false,30,"ONLINE",n),
            OfficialStationEntity(31,"Joe FM (BE)","https://playerservices.streamtheworld.com/api/livestream-redirect/JOE.mp3","https://www.joe.be","https://www.joe.be/favicon.ico",7,"België","Nederlands","Joe — 70s, 80s & 90s hits!",true,false,31,"ONLINE",n),
            // === Radio Browser API top picks ===
            OfficialStationEntity(32,"Intense Radio Dance","https://stream.intenseradio.net/live","https://www.intenseradio.net","",3,"Nederland","Nederlands","We love Dance — HQ FLAC 1411kbps!",true,true,32,"ONLINE",n),
            OfficialStationEntity(33,"Sky Radio 80s Hits","https://playerservices.streamtheworld.com/api/livestream-redirect/SRGSTR04.mp3","https://www.skyradio.nl","",7,"Nederland","Nederlands","Sky Radio — de beste 80s hits!",true,false,33,"ONLINE",n),
            OfficialStationEntity(34,"NPO Radio 2 Soul & Jazz","https://icecast.omroep.nl/radio2-souljazz-mp3","https://www.nporadio2.nl","",8,"Nederland","Nederlands","NPO Radio 2 — soul & jazz",true,true,34,"ONLINE",n),
            OfficialStationEntity(35,"Hi On Line Lounge","https://stream.hionline.eu:8443/lounge","https://www.hionline.eu","",12,"Nederland","Engels","320kbps lounge & chill-out",true,false,35,"ONLINE",n),
            OfficialStationEntity(36,"Concertzender Baroque","https://stream.concertzender.nl/baroque","https://www.concertzender.nl","",8,"Nederland","Nederlands","Klassieke barokmuziek",true,false,36,"ONLINE",n),
            OfficialStationEntity(37,"Fantasy Italo Dance 90s","https://stream.fantasy-italo-dance-90s.nl/1","https://www.fantasy-italo-dance-90s.nl","",3,"Nederland","Nederlands","90s Italo dance & eurobeat!",true,false,37,"ONLINE",n),
            OfficialStationEntity(38,"C-Dance RETRO","https://stream.c-dance.nl/retro","https://www.c-dance.nl","",3,"Nederland","Nederlands","Retro dance, house, trance & techno",true,false,38,"ONLINE",n),
            OfficialStationEntity(39,"Tomorrowland OWR","https://playerservices.streamtheworld.com/api/livestream-redirect/OWR_INTERNATIONAL.mp3","https://www.tomorrowland.com","",3,"België","Engels","Tomorrowland — One World Radio 256kbps",true,true,39,"ONLINE",n),
            OfficialStationEntity(40,"Radio Continu","https://stream.radiocontinu.nl/radiocontinu","https://www.radiocontinu.nl","",6,"Nederland","Nederlands","Nederlandstalige klassiekers en variatie",true,false,40,"ONLINE",n),
            OfficialStationEntity(41,"Arrow Classic Rock","https://stream.gal.io/arrow","https://www.arrow.nl","",2,"Nederland","Nederlands","Arrow Classic Rock — 192kbps MP3",true,true,41,"ONLINE",n),
            OfficialStationEntity(42,"Nostalgie 80s","https://stream.nostalgie80s.be/stream","https://www.nostalgie.eu","",7,"België","Nederlands","Nostalgie — de beste 80s hits!",true,false,42,"ONLINE",n),
            OfficialStationEntity(43,"NRJ België","https://stream.nrj.be/nrj.mp3","https://www.nrj.be","",1,"België","Nederlands","NRJ — hits & top 40!",true,false,43,"ONLINE",n),
            OfficialStationEntity(44,"Klara","https://icecast.vrtcdn.be/klara-high.mp3","https://www.klara.be","",8,"België","Nederlands","Klara — klassiek, jazz & cultuur",true,false,44,"ONLINE",n),
            OfficialStationEntity(45,"RTBF Classic 21","https://stream.rtbf.be/classic21.mp3","https://www.rtbf.be/classic21","",2,"België","Frans","Classic 21 — pop & rock",true,false,45,"ONLINE",n),
            OfficialStationEntity(99,"FreeMinds FM","","","",13,"Nederland","Nederlands","Het officiële FreeMinds radiostation — in te vullen door beheerder",false,false,99,"UNKNOWN",0),
        ).forEach { stationDao.insert(it) }
    }
}
