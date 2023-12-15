package com.example.myapplication

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.Preferences

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object PreferencesUtil {

    val EMAIL = stringPreferencesKey("email")
    val TOKEN = stringPreferencesKey("token")

    suspend fun saveEmailAndToken(email: String, token: String) {
        App.application.applicationContext.dataStore.edit { settings ->
            settings[EMAIL] = email
            settings[TOKEN] = token
        }
    }

    fun readEmail(): Flow<String> {
        return App.application.applicationContext.dataStore.data
            .map { preferences ->
                preferences[EMAIL] ?: ""
            }
    }

    fun readToken(): Flow<String> {
        return App.application.applicationContext.dataStore.data
            .map { preferences ->
                preferences[TOKEN] ?: ""
            }
    }

    suspend fun endSession() {
        App.application.applicationContext.dataStore.edit { settings ->
            settings.remove(EMAIL)
            settings.remove(TOKEN)
        }
    }

}
