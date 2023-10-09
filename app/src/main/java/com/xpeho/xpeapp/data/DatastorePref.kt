package com.xpeho.xpeapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DatastorePref(private val context: Context) {

    companion object {
        private const val PREF_NAME = "XPE_PREF"
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREF_NAME)
        val CONNECT = stringPreferencesKey("isConnectedLeastOneTime")
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
}
