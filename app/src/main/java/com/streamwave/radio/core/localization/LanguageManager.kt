package com.streamwave.radio.core.localization

import android.content.Context
import android.os.Build
import androidx.core.os.LocaleListCompat
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

        val SUPPORTED_LANGUAGES = listOf(
            Locale(LANG_NL),
            Locale(LANG_EN),
            Locale(LANG_DE),
            Locale(LANG_ES)
        )
    }

    fun getCurrentLanguage(): String {
        return runBlocking {
            settingsDataStore.language.first()
        }
    }

    fun detectDeviceLanguage(): String {
        val deviceLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            context.resources.configuration.locale
        }

        val deviceLang = deviceLocale.language
        return if (SUPPORTED_LANGUAGES.any { it.language == deviceLang }) {
            deviceLang
        } else {
            LANG_EN // fallback
        }
    }

    fun getInitialLanguage(): String {
        val saved = getCurrentLanguage()
        return if (saved == LANG_SYSTEM) {
            detectDeviceLanguage()
        } else {
            saved
        }
    }

    suspend fun setLanguage(language: String) {
        settingsDataStore.setLanguage(language)
    }

    fun getLocaleForLanguage(language: String): Locale {
        return when (language) {
            LANG_NL -> Locale(LANG_NL)
            LANG_DE -> Locale(LANG_DE)
            LANG_ES -> Locale(LANG_ES)
            else -> Locale(LANG_EN)
        }
    }
}
