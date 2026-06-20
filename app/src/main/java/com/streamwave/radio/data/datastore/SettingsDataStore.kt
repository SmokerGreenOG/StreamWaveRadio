package com.streamwave.radio.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "streamwave_settings")

class SettingsDataStore(private val context: Context) {

    companion object {
        val KEY_LANGUAGE = stringPreferencesKey("language")
        val KEY_SLEEP_TIMER = stringPreferencesKey("sleep_timer")
        val KEY_ADMIN_PIN = stringPreferencesKey("admin_pin")
        val KEY_THEME = stringPreferencesKey("theme")
        const val DEFAULT_LANGUAGE = "system"
    }

    val language: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_LANGUAGE] ?: DEFAULT_LANGUAGE
    }

    suspend fun setLanguage(language: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_LANGUAGE] = language
        }
    }

    suspend fun setSleepTimer(minutes: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_SLEEP_TIMER] = minutes
        }
    }

    suspend fun setAdminPin(pin: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_ADMIN_PIN] = pin
        }
    }

    val adminPin: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[KEY_ADMIN_PIN]
    }

    val theme: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_THEME] ?: "0"
    }

    suspend fun setTheme(index: String) {
        context.dataStore.edit { prefs -> prefs[KEY_THEME] = index }
    }
}
