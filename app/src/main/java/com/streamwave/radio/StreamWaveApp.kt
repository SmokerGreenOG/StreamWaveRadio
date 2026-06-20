package com.streamwave.radio

import android.app.Application
import com.streamwave.radio.core.localization.LanguageManager
import com.streamwave.radio.core.theme.applyTheme
import com.streamwave.radio.data.database.AppDatabase
import com.streamwave.radio.data.datastore.SettingsDataStore
import com.streamwave.radio.data.database.entity.CategoryEntity
import com.streamwave.radio.data.repository.RadioBrowserRepository
import com.streamwave.radio.sync.SyncWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class StreamWaveApp : Application() {

    @Inject lateinit var db: AppDatabase
    @Inject lateinit var languageManager: LanguageManager
    @Inject lateinit var settingsDataStore: SettingsDataStore
    @Inject lateinit var radioBrowserRepository: RadioBrowserRepository

    override fun onCreate() {
        super.onCreate()
        languageManager.applySavedLanguage()
        kotlinx.coroutines.runBlocking {
            val idx = settingsDataStore.theme.first().toIntOrNull() ?: 0
            applyTheme(idx)
        }
        CoroutineScope(Dispatchers.IO).launch {
            prepopulateCategories()
            loadStationsFromApi()
        }
        // Plan periodieke sync (elke 6 uur)
        SyncWorker.schedule(this)
    }

    private suspend fun prepopulateCategories() {
        val catDao = db.categoryDao()
        val existing = catDao.getAll().firstOrNull()
        if (existing != null && existing.isNotEmpty()) return

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
    }

    private suspend fun loadStationsFromApi() {
        val stationDao = db.officialStationDao()
        val existing = stationDao.getAny(1)
        if (existing.isNotEmpty()) return // Al geladen

        // Laad van 4 landen via Radio Browser API
        radioBrowserRepository.refreshStations(listOf("NL", "DE", "US", "BE"))
    }
}
