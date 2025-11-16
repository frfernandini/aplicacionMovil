package com.example.aplicacion.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacion.model.EditarPerfilUiState
import com.example.aplicacion.model.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditarPerfilViewModel(
    application: Application,
    private val prefsRepository: UserPreferencesRepository
) : AndroidViewModel(application) {

    private val _editarPerfilState = MutableStateFlow(EditarPerfilUiState())
    val editarPerfilState = _editarPerfilState.asStateFlow()

    init {
        viewModelScope.launch {
            // Cargar solo la URI de la imagen guardada localmente
            val savedImageUri = prefsRepository.profileImageUri.first()
            _editarPerfilState.update {
                it.copy(imagenUri = savedImageUri ?: "")
            }
        }
    }

    fun onImagenSeleccionada(uri: Uri) {
        // Actualiza el estado de la UI con la nueva URI para que se vea el cambio al instante
        _editarPerfilState.update { it.copy(imagenUri = uri.toString()) }
    }

    fun guardarCambios() {
        // Lógica simplificada: solo guardar la imagen localmente
        viewModelScope.launch {
            val imageUriToSave = _editarPerfilState.value.imagenUri
            if (imageUriToSave.isNotBlank()) {
                prefsRepository.saveProfileImageUri(imageUriToSave)
            }
            // Marcar como guardado para que la pantalla navegue hacia atrás
            _editarPerfilState.update { it.copy(guardadoExitoso = true) } // CORREGIDO
        }
    }

    fun onNavegacionRealizada() {
        _editarPerfilState.update { it.copy(guardadoExitoso = false) }
    }
}
