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
        val SUPPORTED_LOCALES = listOf(Locale(LANG_NL), Locale(LANG_EN), Locale(LANG_DE), Locale(LANG_ES))

        fun getLocaleForCode(code: String): Locale = when (code) {
            LANG_NL -> Locale(LANG_NL); LANG_DE -> Locale(LANG_DE); LANG_ES -> Locale(LANG_ES)
            else -> Locale(LANG_EN)
        }

        fun wrapContextWithLocale(context: Context, locale: Locale): Context {
            Locale.setDefault(locale)
            val config = Configuration(context.resources.configuration)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                config.setLocale(locale)
            } else {
                @Suppress("DEPRECATION") config.locale = locale
            }
            return context.createConfigurationContext(config)
        }
    }

    @Volatile var currentLocale: Locale = Locale(LANG_EN)

    fun getCurrentLanguage(): String = runBlocking {
        settingsDataStore.language.first()
    }

    fun detectDeviceLanguage(): String {
        val deviceLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            context.resources.configuration.locales[0]
        else @Suppress("DEPRECATION") context.resources.configuration.locale
        return if (SUPPORTED_LOCALES.any { it.language == deviceLocale.language }) deviceLocale.language else LANG_EN
    }

    fun getInitialLanguage(): String {
        val saved = getCurrentLanguage()
        return if (saved == LANG_SYSTEM) detectDeviceLanguage() else saved
    }

    suspend fun setLanguage(language: String) {
        settingsDataStore.setLanguage(language)
    }

    fun applyAndGetContext(language: String): Context {
        val locale = getLocaleForCode(language)
        currentLocale = locale
        return wrapContextWithLocale(context, locale)
    }

    fun applySavedLanguage() {
        val lang = getInitialLanguage()
        currentLocale = getLocaleForCode(lang)
        Locale.setDefault(currentLocale)
    }
}
