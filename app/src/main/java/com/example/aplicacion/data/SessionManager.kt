package com.example.aplicacion.data

/**
 * Un objeto singleton simple para mantener en memoria los datos de la sesión del usuario.
 */
object SessionManager {
    var authToken: String? = null
    var userId: String? = null // <-- AÑADIDO
}
