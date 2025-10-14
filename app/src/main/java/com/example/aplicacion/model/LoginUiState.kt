package com.example.aplicacion.model

data class LoginUiState (
    val correo:String = "",
    val clave:String = "",
    val error: String? = null,
    val loginExitoso: Boolean = false
)