package com.frxhaikal_plg.ingrevia.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.frxhaikal_plg.ingrevia.data.local.model.UserInfo

class UserPreferences(private val context: Context) {

    companion object {
        private val Context.dataStore by preferencesDataStore(name = "ingrevia_preferences")
        private val TOKEN_KEY = stringPreferencesKey("user_token")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val EMAIL_KEY = stringPreferencesKey("user_email")
        private val DISPLAY_NAME_KEY = stringPreferencesKey("user_display_name")
        private val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")
        private val HAS_USER_INFO_KEY = booleanPreferencesKey("has_user_info")
        private val HEIGHT_KEY = stringPreferencesKey("height")
        private val WEIGHT_KEY = stringPreferencesKey("weight")
        private val AGE_KEY = stringPreferencesKey("age")
        private val ACTIVITY_LEVEL_KEY = stringPreferencesKey("activity_level")
        private val GENDER_KEY = stringPreferencesKey("gender")
    }

    suspend fun saveUserSession(
        token: String,
        userId: String,
        email: String,
        displayName: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[USER_ID_KEY] = userId
            preferences[EMAIL_KEY] = email
            preferences[DISPLAY_NAME_KEY] = displayName
            preferences[IS_LOGGED_IN_KEY] = true
        }
    }

    val userToken: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[TOKEN_KEY]
        }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_LOGGED_IN_KEY] ?: false
        }

    suspend fun clearUserSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun setLoggedIn(isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN_KEY] = isLoggedIn
        }
    }

    suspend fun saveUserInfo(userInfo: UserInfo) {
        context.dataStore.edit { preferences ->
            preferences[HEIGHT_KEY] = userInfo.height.toString()
            preferences[WEIGHT_KEY] = userInfo.weight.toString()
            preferences[AGE_KEY] = userInfo.age.toString()
            preferences[ACTIVITY_LEVEL_KEY] = userInfo.activityLevel.toString()
            preferences[GENDER_KEY] = userInfo.gender
            preferences[HAS_USER_INFO_KEY] = true
        }
    }

    val hasUserInfo: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[HAS_USER_INFO_KEY] ?: false
        }

    val height: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[HEIGHT_KEY] ?: "0"
        }

    val weight: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[WEIGHT_KEY] ?: "0"
        }

    val age: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[AGE_KEY] ?: "0"
        }

    val activityLevel: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[ACTIVITY_LEVEL_KEY] ?: "0"
        }

    val gender: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[GENDER_KEY] ?: ""
        }
} 