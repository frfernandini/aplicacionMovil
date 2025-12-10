package com.example.aplicacion.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object SessionManager {
    var authToken: String? = null
    var userId: String? = null
    var userName: String? = null
    var userEmail: String? = null

    // ARREGLO: Convertir la URL de la imagen a un StateFlow para que se pueda observar.
    private val _userImageUrl = MutableStateFlow<String?>(null)
    val userImageUrl = _userImageUrl.asStateFlow()

    fun setUserImageUrl(url: String?) {
        _userImageUrl.value = url
    }

    fun clearSession() {
        authToken = null
        userId = null
        userName = null
        userEmail = null
        _userImageUrl.value = null // Resetear el StateFlow al cerrar sesión
    }
}
