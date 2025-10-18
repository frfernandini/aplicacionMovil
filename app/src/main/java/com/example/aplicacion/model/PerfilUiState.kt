package com.example.aplicacion.model

data class PerfilUiState (
    val nombreUsuario: String = "",
    val email: String = "",
    val imagenUri:String = "",
    val logoutExitoso: Boolean = false
)