package com.example.aplicacion.data.remote.dto

data class BlogDto(
    val id: Long,
    val titulo: String,
    val contenido: String,
    val autor: String?,
    val fecha: String,
    val imagenUrl: String?
)