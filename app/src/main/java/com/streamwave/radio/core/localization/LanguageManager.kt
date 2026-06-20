package com.streamwave.radio.core.localization

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import com.streamwave.radio.data.datastore.SettingsDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguageManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsDataStore: SettingsDataStore
) {
    companion object {
        const val LANG_NL = "nl"
        const val LANG_EN = "en"
        const val LANG_DE = "de"
        const val LANG_ES = "es"
        const val LANG_SYSTEM = "system"

        val SUPPORTED_LANGUAGES = listOf(Locale(LANG_NL), Locale(LANG_EN), Locale(LANG_DE), Locale(LANG_ES))

        // Statische cache voor directe beschikbaarheid (overleeft app restart niet, maar we slaan op in DataStore)
        @Volatile var pendingLanguage: String? = null

        fun applyLocale(context: Context, language: String) {
            val locale = when (language) {
                LANG_NL -> Locale(LANG_NL)
                LANG_DE -> Locale(LANG_DE)
                LANG_ES -> Locale(LANG_ES)
                else -> Locale(LANG_EN)
            }
            Locale.setDefault(locale)
            val config = Configuration(context.resources.configuration)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                config.setLocale(locale)
            } else {
                @Suppress("DEPRECATION")
                config.locale = locale
            }
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            // Sla direct op in static cache
            pendingLanguage = language
        }
    }

    fun getCurrentLanguage(): String {
        // Check static cache eerst
        pendingLanguage?.let { return it }
        return runBlocking { settingsDataStore.language.first() }
    }

    fun detectDeviceLanguage(): String {
        val deviceLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            @Suppress("DEPRECATION") context.resources.configuration.locale
        }
        val deviceLang = deviceLocale.language
        return if (SUPPORTED_LANGUAGES.any { it.language == deviceLang }) deviceLang else LANG_EN
    }

    fun getInitialLanguage(): String {
        val saved = getCurrentLanguage()
        if (saved == LANG_SYSTEM) return detectDeviceLanguage()
        return saved
    }

    suspend fun setLanguage(language: String) {
        pendingLanguage = language
        settingsDataStore.setLanguage(language)
    }

    fun applySavedLanguage() {
        val lang = getInitialLanguage()
        applyLocale(context, lang)
    }
}
