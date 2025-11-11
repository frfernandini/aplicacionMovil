package com.example.aplicacion.data.remote.dto

data class RegistroRequest(
    val nombre: String,
    val email: String,
    val password: String,
    val direccion: String
)
