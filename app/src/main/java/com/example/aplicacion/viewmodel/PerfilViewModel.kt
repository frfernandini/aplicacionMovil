package com.example.aplicacion.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacion.data.SessionManager
import com.example.aplicacion.model.PerfilUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PerfilViewModel(application: Application) : AndroidViewModel(application) {

    private val _perfilState = MutableStateFlow(PerfilUiState())
    val perfilState: StateFlow<PerfilUiState> = _perfilState.asStateFlow()

    init {
        // ARREGLO: Nos suscribimos a los cambios del StateFlow de la imagen.
        viewModelScope.launch {
            SessionManager.userImageUrl.collect { imageUrl ->
                _perfilState.update {
                    it.copy(
                        nombreUsuario = SessionManager.userName ?: "Usuario",
                        email = SessionManager.userEmail ?: "Sin correo",
                        imagenUri = imageUrl ?: ""
                    )
                }
            }
        }
    }

    fun onCerrarSesion() {
        viewModelScope.launch {
            // Limpiar la sesión en el SessionManager
            SessionManager.clearSession()
            _perfilState.update { it.copy(logoutExitoso = true) }
        }
    }

    fun onNavegacionRealizada() {
        _perfilState.update { it.copy(logoutExitoso = false) }
    }
}