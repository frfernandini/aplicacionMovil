package com.example.aplicacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.example.aplicacion.model.LoginUiState
import com.example.aplicacion.model.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val repo: UsuarioRepository): ViewModel() {
    private val _loginState = MutableStateFlow(LoginUiState())
    val loginState = _loginState.asStateFlow()

    fun onCorreoChange(correo: String) {
        _loginState.update { it.copy(correo = correo) }
    }

    fun onClaveChange(clave: String) {
        _loginState.update { it.copy(clave = clave) }
    }

    fun iniciarSesion() {
        if(_loginState.value.correo.isBlank() || _loginState.value.clave.isBlank()){
            _loginState.update { it.copy(error = "correo o contraseña vacios") }
            return
        }
        viewModelScope.launch {
            val usuario = repo.obtenerPorCorreo(_loginState.value.correo)

            if(usuario == null){
                _loginState.update { it.copy(error = "Usuario no encontrado") }
            }else if(usuario.contrasena != _loginState.value.clave){
                _loginState.update { it.copy(error = "Contraseña incorrecta") }
            }else{
                _loginState.update{it.copy(error = null,loginExitoso = true) }
            }
        }
    }
    fun onNavegacionRealizada() {
        _loginState.update { it.copy(loginExitoso = false) }
    }
}