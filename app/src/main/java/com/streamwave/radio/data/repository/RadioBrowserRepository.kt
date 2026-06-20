package com.streamwave.radio.data.repository

import com.streamwave.radio.data.database.dao.OfficialStationDao
import com.streamwave.radio.data.database.entity.OfficialStationEntity
import com.streamwave.radio.network.api.RadioBrowserApi
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RadioBrowserRepository @Inject constructor(
    private val api: RadioBrowserApi,
    private val stationDao: OfficialStationDao
) {
    /**
     * Haalt stations op van de API en cached ze in Room.
     * Gebruikt countryCode: NL, DE, US, BE
     */
    suspend fun refreshStations(countryCodes: List<String> = listOf("NL", "DE", "US", "BE")) {
        val allStations = mutableListOf<OfficialStationEntity>()
        var nextId = (stationDao.getMaxId().firstOrNull() ?: 0) + 1

        for (code in countryCodes) {
            try {
                val apiStations = api.searchStations(countryCode = code)
                for (s in apiStations) {
                    if (s.url.isEmpty() || s.name.isEmpty()) continue
                    // Skip entries die al bestaan (op basis van URL)
                    val exists = stationDao.getByUrl(s.url).firstOrNull()
                    if (exists != null) continue

                    val categoryId = mapTagsToCategory(s.tags)
                    val lang = mapLanguage(s.language)
                    val country = mapCountry(s.countrycode)

                    allStations.add(
                        OfficialStationEntity(
                            id = nextId++,
                            name = s.name,
                            streamUrl = s.url_resolved.ifEmpty { s.url },
                            websiteUrl = s.homepage,
                            logoUrl = s.favicon,
                            categoryId = categoryId,
                            country = country,
                            language = lang,
                            description = "${s.tags} — ${s.bitrate}kbps ${s.codec}",
                            isActive = s.lastcheckok > 0,
                            isFeatured = false,
                            sortOrder = nextId.toInt(),
                            streamStatus = if (s.lastcheckok > 0) "ONLINE" else "UNKNOWN",
                            lastCheckedAt = System.currentTimeMillis()
                        )
                    )
                }
            } catch (e: Exception) {
                // Skip landen die offline zijn
                continue
            }
        }

        if (allStations.isNotEmpty()) {
            allStations.forEach { stationDao.insert(it) }
        }
    }

    private fun mapTagsToCategory(tags: String): Long = when {
        tags.contains("pop", true) || tags.contains("top 40", true) -> 1
        tags.contains("rock", true) || tags.contains("alternative", true) -> 2
        tags.contains("dance", true) || tags.contains("electronic", true) || tags.contains("house", true) || tags.contains("techno", true) -> 3
        tags.contains("hip hop", true) || tags.contains("rap", true) || tags.contains("rnb", true) -> 4
        tags.contains("reggae", true) -> 5
        tags.contains("dutch", true) || tags.contains("nederlands", true) -> 6
        tags.contains("classic", true) || tags.contains("oldies", true) || tags.contains("80s", true) || tags.contains("90s", true) -> 7
        tags.contains("jazz", true) || tags.contains("blues", true) || tags.contains("soul", true) -> 8
        tags.contains("news", true) || tags.contains("talk", true) -> 9
        tags.contains("talk", true) -> 10
        tags.contains("hardcore", true) || tags.contains("metal", true) || tags.contains("hardstyle", true) -> 11
        tags.contains("chill", true) || tags.contains("ambient", true) || tags.contains("lounge", true) -> 12
        tags.contains("internet", true) -> 13
        else -> 15 // Overig
    }

    private fun mapLanguage(lang: String): String = when (lang.lowercase()) {
        "dutch" -> "Nederlands"; "english" -> "Engels"; "german" -> "Duits"
        "spanish" -> "Spaans"; "french" -> "Frans"; "italian" -> "Italiaans"
        else -> lang.replaceFirstChar { it.uppercase() }
    }

    private fun mapCountry(code: String): String = when (code.uppercase()) {
        "NL" -> "Nederland"; "DE" -> "Duitsland"; "US" -> "Verenigde Staten"
        "BE" -> "België"; "GB" -> "Verenigd Koninkrijk"; "FR" -> "Frankrijk"
        else -> code.uppercase()
    }
}
