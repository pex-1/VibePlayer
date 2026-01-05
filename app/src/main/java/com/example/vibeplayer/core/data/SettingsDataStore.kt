package com.example.vibeplayer.core.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.vibeplayer.core.domain.SettingsDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsDataStoreImpl(private val context: Context) : SettingsDataStore {

    private companion object {
        private val Context.settingsDataStore by preferencesDataStore(
            name = "settings_datastore"
        )
    }

    private val durationKey = intPreferencesKey("minimum_duration")
    private val sizeKey = intPreferencesKey("minimum_size")

    override suspend fun setDefaultDuration(duration: Int) {
        context.settingsDataStore.edit { settings ->
            settings[durationKey] = duration
        }
    }

    override fun getDefaultDuration(): Flow<Int> {
        return context.settingsDataStore.data
            .map { preferences ->
                preferences[durationKey] ?: 30
            }
    }

    override suspend fun setDefaultSize(size: Int) {
        context.settingsDataStore.edit { settings ->
            settings[sizeKey] = size
        }
    }

    override fun getDefaultSize(): Flow<Int> {
        return context.settingsDataStore.data
            .map { preferences ->
                preferences[sizeKey] ?: 100
            }
    }
}
