package com.example.aplicacion.data.remote.dto

data class ProductoDto(
    val id: Long,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagenUrl: String, // ¡Importante! Ahora es una String para la URL
    val categoria: String
)
