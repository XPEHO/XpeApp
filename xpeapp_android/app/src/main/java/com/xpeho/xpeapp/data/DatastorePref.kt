package com.xpeho.xpeapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.xpeho.xpeapp.domain.AuthData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DatastorePref(private val context: Context) {

    companion object {
        private const val PREF_NAME = "XPE_PREF"
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREF_NAME)
        val CONNECT = stringPreferencesKey("isConnectedLeastOneTime")
        val AUTH_DATA = stringPreferencesKey("authData")
        val WAS_CONNECTED_LAST_TIME = stringPreferencesKey("wasConnectedLastTime")
    }

    val isConnectedLeastOneTime: Flow<Boolean> = context.dataStore.data
        .map { preference ->
            preference[CONNECT]?.toBoolean() ?: false
        }

    suspend fun setIsConnectedLeastOneTime(isConnected: Boolean) {
        context.dataStore.edit { preference ->
            preference[CONNECT] = isConnected.toString()
        }
    }

    suspend fun setUserId(userId: String) {
        context.dataStore.edit { preference ->
            preference[stringPreferencesKey("userId")] = userId
        }
    }

    val userId: Flow<String> = context.dataStore.data
        .map { preference ->
            preference[stringPreferencesKey("userId")] ?: ""
        }

    // AuthData datastore

    suspend fun setAuthData(authData: AuthData) {
        val json = Gson().toJson(authData)
        context.dataStore.edit { preference ->
            preference[AUTH_DATA] = json
        }
    }

    suspend fun getAuthData(): AuthData? {
        val json = context.dataStore.data.map { preferences ->
            preferences[AUTH_DATA]
        }.first()
        return json?.let { Gson().fromJson(it, AuthData::class.java) }
    }

    suspend fun clearAuthData() {
        context.dataStore.edit { preference ->
            preference.remove(AUTH_DATA)
        }
    }

    // wasConnectedLastTime datastore

    suspend fun setWasConnectedLastTime(wasConnected: Boolean) {
        context.dataStore.edit { preference ->
            preference[WAS_CONNECTED_LAST_TIME] = wasConnected.toString()
        }
    }

    suspend fun getWasConnectedLastTime(): Boolean {
        return context.dataStore.data.map { preferences ->
            preferences[WAS_CONNECTED_LAST_TIME]?.toBoolean() ?: false
        }.first()
    }
}

