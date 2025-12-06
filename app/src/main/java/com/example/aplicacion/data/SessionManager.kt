package com.example.aplicacion.data

object SessionManager {
    var authToken: String? = null
    var userId: String? = null
    var userName: String? = null
    var userEmail: String? = null

    fun clearSession() {
        authToken = null
        userId = null
        userName = null
        userEmail = null
    }
}
