package com.example.aplicacion.model.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension property to create the DataStore instance
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepository(context: Context) {

    private val dataStore = context.dataStore

    // Define la clave para guardar la URI de la imagen de perfil
    private object PreferencesKeys {
        val PROFILE_IMAGE_URI = stringPreferencesKey("profile_image_uri")
    }

    /**
     * Guarda la URI de la imagen de perfil de forma local.
     */
    suspend fun saveProfileImageUri(uri: String) {
        dataStore.edit {
            it[PreferencesKeys.PROFILE_IMAGE_URI] = uri
        }
    }

    /**
     * Obtiene un Flow que emite la URI de la imagen de perfil guardada.
     */
    val profileImageUri: Flow<String?> = dataStore.data
        .map {
            it[PreferencesKeys.PROFILE_IMAGE_URI]
        }
}