package com.example.aplicacion.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacion.data.SessionManager
import com.example.aplicacion.model.EditarPerfilUiState
import com.example.aplicacion.model.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// LÓGICA FINAL: Este ViewModel ahora depende de UsuarioRepository para subir la imagen a la red.
class EditarPerfilViewModel(
    application: Application,
    private val repository: UsuarioRepository
) : AndroidViewModel(application) {

    private val _editarPerfilState = MutableStateFlow(EditarPerfilUiState())
    val editarPerfilState = _editarPerfilState.asStateFlow()

    init {
        // Al iniciar, la UI muestra la imagen que ya está en la sesión (puede ser de S3 o una local si se acaba de seleccionar)
        _editarPerfilState.update {
            // ARREGLO: Accedemos al valor actual del StateFlow con .value
            it.copy(imagenUri = SessionManager.userImageUrl.value ?: "")
        }
    }

    fun onImagenSeleccionada(uri: Uri) {
        // Actualiza la UI para la vista previa de la imagen seleccionada.
        _editarPerfilState.update { it.copy(imagenUri = uri.toString()) }
    }

    fun guardarCambios() {
        viewModelScope.launch {
            val userId = SessionManager.userId ?: return@launch
            val imagenUriString = _editarPerfilState.value.imagenUri

            if (imagenUriString.isNotBlank() && imagenUriString.startsWith("content://")) {
                val imagenUri = Uri.parse(imagenUriString)
                
                // Llama a la función del repositorio para subir la imagen al backend.
                val success = repository.subirImagenPerfil(userId, imagenUri)

                if (success) {
                    // Si la subida es exitosa, el repo ya actualizó el SessionManager.
                    // Marcamos para navegar hacia atrás.
                    _editarPerfilState.update { it.copy(guardadoExitoso = true) }
                } else {
                    // Opcional: Manejar el error en la UI (ej. mostrar un Toast)
                }
            } else {
                 // Si la imagen no ha cambiado (no es una uri local), simplemente cerramos.
                 _editarPerfilState.update { it.copy(guardadoExitoso = true) }
            }
        }
    }

    fun onNavegacionRealizada() {
        _editarPerfilState.update { it.copy(guardadoExitoso = false) }
    }
}