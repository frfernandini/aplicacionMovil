package com.example.aplicacion.model

// Simplificado para solo manejar la imagen y el estado de guardado.
data class EditarPerfilUiState(
    val imagenUri: String = "",
    val guardadoExitoso: Boolean = false
)
