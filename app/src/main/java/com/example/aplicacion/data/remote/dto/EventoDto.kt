package com.example.aplicacion.data.remote.dto

data class EventoDto(
    val id: Long,
    val nombre: String,
    val descripcion: String,
    val fechaInicio: String,
    val fechaFin: String?,
    val imagenUrl: String?,
    val lugar: String,
    val categoriaAsociada: String?,
    val latitud: Double? = -33.0444,
    val longitud: Double? = -71.6155
)