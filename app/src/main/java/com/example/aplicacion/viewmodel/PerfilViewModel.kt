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

// PASO 1: Añadir la dependencia del repositorio de preferencias
class PerfilViewModel(
    application: Application,
    private val prefsRepository: UserPreferencesRepository
) : AndroidViewModel(application) {

    private val _perfilState = MutableStateFlow(PerfilUiState())
    val perfilState: StateFlow<PerfilUiState> = _perfilState.asStateFlow()

    init {
        viewModelScope.launch {
            // PASO 2: Observar el Flow de la URI de la imagen
            prefsRepository.profileImageUri.collect { savedImageUri ->
                _perfilState.update {
                    it.copy(
                        nombreUsuario = SessionManager.userName ?: "Usuario",
                        email = SessionManager.userEmail ?: "Sin correo",
                        imagenUri = savedImageUri ?: "" // <-- Se actualiza automáticamente
                    )
                }
            }
        }
    }

    fun onCerrarSesion() {
        viewModelScope.launch {
            // Limpiar la sesión en el SessionManager
            SessionManager.clearSession()
            // Limpiar también la imagen guardada en DataStore
            prefsRepository.saveProfileImageUri("")
            _perfilState.update { it.copy(logoutExitoso = true) }
        }
    }

    fun onNavegacionRealizada() {
        _perfilState.update { it.copy(logoutExitoso = false) }
    }
}
