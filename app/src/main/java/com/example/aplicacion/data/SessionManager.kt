package com.example.aplicacion.data

object SessionManager {
    var authToken: String? = null
    var userId: String? = null
    var userName: String? = null // Añadido
    var userEmail: String? = null // Añadido

    fun clearSession() {
        authToken = null
        userId = null
        userName = null
        userEmail = null
    }
}
