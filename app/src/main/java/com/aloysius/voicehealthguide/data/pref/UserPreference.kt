package com.aloysius.voicehealthguide.data.pref

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.aloysius.voicehealthguide.di.TokenManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: UserModel) {
        Log.d("UserPreference", "Saving session with token: ${user.token}")
        dataStore.edit { preferences ->
            preferences[NAME] = user.name
            preferences [USER_NAME] = user.username
            preferences[EMAIL] = user.email
            preferences[TOKEN_KEY] = user.token
            preferences[IS_LOGIN_KEY] = true
            preferences[TIMESTAMP_KEY] = System.currentTimeMillis()
        }
        TokenManager.setToken(user.token)
    }


    private fun isSessionExpired(timestamp: Long): Boolean {
        val currentTime = System.currentTimeMillis()
        val oneHourInMillis = TimeUnit.HOURS.toMillis(1)
        return currentTime - timestamp > oneHourInMillis
    }

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            val timestamp = preferences[TIMESTAMP_KEY] ?: 0L
            if (isSessionExpired(timestamp)) {
                logout()
                UserModel( "","","","", false, 0L)
            } else {
                UserModel(
                    preferences[NAME] ?: "",
                    preferences[USER_NAME] ?: "",
                    preferences[EMAIL] ?: "",
                    preferences[TOKEN_KEY] ?: "",
                    preferences[IS_LOGIN_KEY] ?: false
                ).also { session ->
                    TokenManager.setToken(session.token)
                }
            }
        }
    }



    suspend fun logout() {
        dataStore.edit { preferences ->
            Log.d("DataStore", "Clearing session, current preferences: $preferences")
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null
        private val NAME = stringPreferencesKey("name")
        private val USER_NAME = stringPreferencesKey("username")
        private val EMAIL = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")
        private val TIMESTAMP_KEY = longPreferencesKey("timestamp")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
