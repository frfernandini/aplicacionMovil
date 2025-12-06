package com.example.aplicacion.data.remote.dto

data class AuthResponse(
    val token: String,
    val id: String,
    val nombre: String,
    val imagenUrl: String? // <-- CAMPO AÑADIDO
)