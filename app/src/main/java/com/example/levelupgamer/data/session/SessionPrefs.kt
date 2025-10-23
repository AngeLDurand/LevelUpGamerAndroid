package com.example.levelupgamer.data.session

import android.content.Context
import android.net.Uri
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "session")



class SessionPrefs(private val context: Context) {
    private val KEY_EMAIL = stringPreferencesKey("current_email")
    private val KEY_PHOTO = stringPreferencesKey("profile_photo_uri")

    val profilePhotoUri: Flow<String?> = context.dataStore.data.map { it[KEY_PHOTO] }

    val currentEmail: Flow<String?> =
        context.dataStore.data.map { it[KEY_EMAIL] }

    suspend fun setCurrentEmail(email: String) {
        context.dataStore.edit { it[KEY_EMAIL] = email }
    }

    suspend fun clear() {
        context.dataStore.edit { it.remove(KEY_EMAIL) }
    }

    suspend fun setProfilePhoto(uri: Uri?) {
        context.dataStore.edit { prefs ->
            if (uri == null) prefs.remove(KEY_PHOTO) else prefs[KEY_PHOTO] = uri.toString()
        }
    }
}
