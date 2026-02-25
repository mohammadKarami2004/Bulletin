package com.example.newsflow.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SettingDataStore(private val dataStore: DataStore<Preferences>) {

    companion object {
        val IS_DARK_MODE = booleanPreferencesKey("isDarkMode")
    }

    suspend fun changeThemeMode(state: Boolean) {
        dataStore.edit {
            it[IS_DARK_MODE] = state
        }
    }

    fun isDarkMode(): Flow<Boolean> {
        return dataStore.data.map { it[IS_DARK_MODE] ?: false }
    }

}