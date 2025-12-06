package com.example.aplicacion.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacion.data.SessionManager
import com.example.aplicacion.model.PerfilUiState
import com.example.aplicacion.model.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// CAMBIO: Se vuelve a añadir la dependencia del repositorio de preferencias local
class PerfilViewModel(
    application: Application,
    private val prefsRepository: UserPreferencesRepository
) : AndroidViewModel(application) {

    private val _perfilState = MutableStateFlow(PerfilUiState())
    val perfilState: StateFlow<PerfilUiState> = _perfilState.asStateFlow()

    init {
        viewModelScope.launch {
            // Escucha cambios en la imagen guardada localmente
            prefsRepository.profileImageUri.collect { localImageUri ->
                _perfilState.update {
                    it.copy(
                        nombreUsuario = SessionManager.userName ?: "Usuario",
                        email = SessionManager.userEmail ?: "Sin correo",
                        // LÓGICA HÍBRIDA: Usa la imagen local si existe, si no, la del backend (sesión).
                        imagenUri = localImageUri?.takeIf { uri -> uri.isNotBlank() } ?: SessionManager.userImageUrl ?: ""
                    )
                }
            }
        }
    }

    fun onCerrarSesion() {
        viewModelScope.launch {
            // Limpiar la sesión en el SessionManager
            SessionManager.clearSession()
            // Limpiar también la imagen guardada localmente para el próximo usuario
            prefsRepository.saveProfileImageUri("")
            _perfilState.update { it.copy(logoutExitoso = true) }
        }
    }

    fun onNavegacionRealizada() {
        _perfilState.update { it.copy(logoutExitoso = false) }
    }
}
