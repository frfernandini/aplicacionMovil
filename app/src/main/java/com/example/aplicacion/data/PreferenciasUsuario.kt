package com.example.aplicacion.data
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore : DataStore<Preferences> by preferencesDataStore("preferencias_usuario")
class EstadoPreferenciasDataStore(private val context: Context){
    private object PreferencesKeys{
        val _correo = stringPreferencesKey("correo")
        val _nombre = stringPreferencesKey("nombre")
        val _imagenUri = stringPreferencesKey("imagen_uri")
    }

    val correo:Flow<String?> =  context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys._correo]
        }

    val nombre: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys._nombre]
        }
    val imagenUri:Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys._imagenUri]
        }
    suspend fun guardarEstado(email: String,nombre: String,imagenUri : String? = null){
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys._correo] = email
            preferences[PreferencesKeys._nombre] = nombre
            if(imagenUri != null){
                preferences[PreferencesKeys._imagenUri] = imagenUri
            }
        }
    }

    suspend fun borrarEstado(){
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}