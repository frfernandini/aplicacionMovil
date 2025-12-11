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

    init {
        // --- PERSISTENCIA LOCAL: Cargar último usuario ---
        viewModelScope.launch {
            val usuarioGuardado = repo.obtenerUltimoUsuarioLocal()
            if (usuarioGuardado != null) {
                _loginState.update { it.copy(correo = usuarioGuardado.correo) }
            }
        }
    }

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
                    SessionManager.authToken = authResponse.token
                    SessionManager.userId = authResponse.id
                    SessionManager.userName = authResponse.nombre
                    SessionManager.userEmail = correo
                    
                    SessionManager.setUserImageUrl(authResponse.imagenUrl)

                    // --- PERSISTENCIA LOCAL: Guardar usuario para la próxima vez ---
                    // Guardamos el usuario localmente para recordar su correo
                    repo.guardarUsuarioLocal(
                        id = authResponse.id,
                        nombre = authResponse.nombre,
                        email = correo
                    )
                    // -----------------------------------------------------------

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
        _loginState.update { it.copy(loginExitoso = false) }
    }
}
