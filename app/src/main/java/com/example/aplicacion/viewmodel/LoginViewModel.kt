package com.example.aplicacion.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacion.data.SessionManager
import com.example.aplicacion.data.remote.dto.LoginRequest
import com.example.aplicacion.model.LoginUiState
import com.example.aplicacion.model.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(application: Application, private val repo: UsuarioRepository) : AndroidViewModel(application) {
    private val _loginState = MutableStateFlow(LoginUiState())
    val loginState = _loginState.asStateFlow()

    fun onCorreoChange(correo: String) {
        _loginState.update { it.copy(correo = correo) }
    }

    fun onClaveChange(clave: String) {
        _loginState.update { it.copy(clave = clave) }
    }

    fun iniciarSesion() {
        val correo = _loginState.value.correo
        val clave = _loginState.value.clave

        if (correo.isBlank() || clave.isBlank()) {
            _loginState.update { it.copy(error = "Correo o contraseña no pueden estar vacíos") }
            return
        }

        viewModelScope.launch {
            _loginState.update { it.copy(error = null) }
            try {
                val loginRequest = LoginRequest(correo, clave)
                val authResponse = repo.loginUsuario(loginRequest)

                if (authResponse != null && authResponse.token.isNotBlank() && authResponse.id.isNotBlank()) {
                    // Guardar TODA la información de la sesión
                    SessionManager.authToken = authResponse.token
                    SessionManager.userId = authResponse.id
                    SessionManager.userName = authResponse.nombre
                    SessionManager.userEmail = correo // El email lo tenemos del formulario
                    SessionManager.userImageUrl = authResponse.imagenUrl // <-- CAMPO AÑADIDO

                    // Actualiza el estado de la UI para la navegación
                    _loginState.update {
                        it.copy(
                            loginExitoso = true,
                            userId = authResponse.id
                        )
                    }

                } else {
                    _loginState.update { it.copy(error = "Credenciales inválidas. Por favor, inténtalo de nuevo.") }
                }

            } catch (e: Exception) {
                _loginState.update { it.copy(error = "Error de conexión: ${e.message}") }
            }
        }
    }

    fun onNavegacionRealizada() {
        // Resetea el estado de navegación para evitar re-navegaciones accidentales
        _loginState.update { it.copy(loginExitoso = false) }
    }
}
