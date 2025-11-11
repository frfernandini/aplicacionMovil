package com.example.aplicacion.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.example.aplicacion.data.EstadoPreferenciasDataStore
import com.example.aplicacion.data.remote.dto.LoginRequest
import com.example.aplicacion.model.LoginUiState
import com.example.aplicacion.model.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(application : Application,private val repo: UsuarioRepository): AndroidViewModel(application) {
    private val preferenciasUsuario = EstadoPreferenciasDataStore(application)
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
            try {
                // Paso 1: Crear el objeto de solicitud para la API
                val loginRequest = LoginRequest(correo, clave)

                // Paso 2: Llamar al repositorio, que a su vez llamará a la API
                val authResponse = repo.loginUsuario(loginRequest) // <-- Necesitarás crear este método en el repositorio

                // Paso 3: Procesar la respuesta del backend
                if (authResponse != null && authResponse.token.isNotBlank()) {
                    // ¡Éxito! El backend validó las credenciales y devolvió un token.
                    // Ahora, opcionalmente, puedes guardar datos localmente.

                    // Ejemplo: Guardar en DataStore
                    preferenciasUsuario.guardarEstado(
                        email = correo,
                        nombre = "NombreDelUsuario", // El backend debería devolver el nombre
                        imagenUri = preferenciasUsuario.imagenUri.first()
                    )

                    _loginState.update { it.copy(error = null, loginExitoso = true) }

                } else {
                    // El backend no devolvió un token o la respuesta fue nula
                    _loginState.update { it.copy(error = "Credenciales inválidas. Por favor, inténtalo de nuevo.") }
                }

            } catch (e: Exception) {
                // Captura errores de red (como el 500, 401, etc.) o de otro tipo
                // Tu repositorio podría manejar esto y devolver una respuesta de error más específica.
                _loginState.update { it.copy(error = "Error de conexión: ${e.message}") }
            }
        }
    }
    fun onNavegacionRealizada() {
        _loginState.update { it.copy(loginExitoso = false) }
    }

}