package com.homeapps.exportvcf.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "exportVCF")

class ExportVCFDataStore(private val appContext: Context) {
    fun getDarkThemeFlow(): Flow<Boolean> = appContext.dataStore.data.map { preferences ->
        preferences[DARK_THEME_KEY] ?: false
    }

    suspend fun setDarkTheme(darkTheme: Boolean) {
        appContext.dataStore.edit { preferences ->
            preferences[DARK_THEME_KEY] = darkTheme
        }
    }

    companion object {
        val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
    }
}