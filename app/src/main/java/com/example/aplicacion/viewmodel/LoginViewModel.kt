package com.example.aplicacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.room.util.copy
import com.example.aplicacion.model.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
class LoginViewModel: ViewModel() {
    private val _loginState = MutableStateFlow(LoginUiState())
    val loginState = _loginState.asStateFlow()

    fun onCorreoChange(correo: String) {
        _loginState.update { it.copy(correo = correo) }
    }

    fun onClaveChange(clave: String) {
        _loginState.update { it.copy(clave = clave) }
    }

    fun iniciarSesion() {
        // Aquí irá tu futura lógica para hablar con la base de datos SQLite
        println("Intentando iniciar sesión con correo: ${_loginState.value.correo}")

        // Simulación de un error para que veas cómo se mostraría
        if (_loginState.value.correo.isBlank() || _loginState.value.clave.isBlank()) {
            _loginState.update { it.copy(error = "Correo y contraseña no pueden estar vacíos") }
            return
        }

        // Lógica de éxito (aquí navegarías a la siguiente pantalla)
        _loginState.update { it.copy(error = null) }
    }
}