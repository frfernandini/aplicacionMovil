package com.example.aplicacion.model

data class EditarPerfilUiState(
    val nombre: String = "",
    val email: String = "",
    val contrasena: String = "",
    val imagenUri: String = "",
    val guardadoExitoso: Boolean = false,
    val error: String? = null
)