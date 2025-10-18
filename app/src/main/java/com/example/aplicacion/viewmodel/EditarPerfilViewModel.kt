package com.example.aplicacion.viewmodel

import android.app.Application
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.animation.core.copy
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacion.data.EstadoPreferenciasDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.aplicacion.model.EditarPerfilUiState
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditarPerfilViewModel(application: Application) : AndroidViewModel(application) {


    private val preferenciasUsuario = EstadoPreferenciasDataStore(application)
    private val _editarPerfilState = MutableStateFlow(EditarPerfilUiState())
    val editarPerfilState = _editarPerfilState.asStateFlow()



    // 4. El bloque 'init' se ejecuta cuando el ViewModel se crea por primera vez
    init {
        cargarDatosActuales()
    }

    // --- FUNCIONES PÚBLICAS (Llamadas desde la UI) ---

    fun onNombreChange(nuevoNombre: String) {
        _editarPerfilState.update { it.copy(nombre = nuevoNombre) }
    }


    fun onContrasenaChange(nuevaContrasena: String) {
        _editarPerfilState.update { it.copy(contrasena = nuevaContrasena) }
    }

    fun onImagenSeleccionada(nuevaUri: Uri) {
        // Guardamos la URI como un String porque es más fácil de serializar y guardar
        _editarPerfilState.update { it.copy(imagenUri = nuevaUri.toString()) }
    }
    fun guardarCambios() {
        viewModelScope.launch {
            // Llama a la función de DataStore para guardar los nuevos datos
            preferenciasUsuario.guardarEstado(
                nombre = _editarPerfilState.value.nombre,
                email = _editarPerfilState.value.email,
                imagenUri = _editarPerfilState.value.imagenUri
            )
            // Actualiza el estado para indicar que se guardó y poder navegar hacia atrás
            _editarPerfilState.update { it.copy(guardadoExitoso = true) }
        }
    }

    fun onNavegacionRealizada() {
        _editarPerfilState.update { it.copy(guardadoExitoso = false) }
    }

    // --- FUNCIONES PRIVADAS (Lógica interna) ---

    private fun cargarDatosActuales() {
        viewModelScope.launch {        val nombreActual = preferenciasUsuario.nombre.first()
            val emailActual = preferenciasUsuario.correo.first()
            // --- AÑADE ESTA LÍNEA ---
            val imagenUriActual = preferenciasUsuario.imagenUri.first()

            _editarPerfilState.update {
                it.copy(
                    nombre = nombreActual ?: "",
                    email = emailActual ?: "",
                    // --- Y AÑADE ESTA LÍNEA ---
                    imagenUri = imagenUriActual ?: ""
                )
            }
        }
    }
}