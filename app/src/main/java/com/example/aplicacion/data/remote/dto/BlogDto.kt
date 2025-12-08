package com.example.aplicacion.data.remote.dto

import com.google.gson.annotations.SerializedName

data class BlogDto(
    val id: Long,
    val titulo: String,
    val contenido: String,
    val autor: String?,
    
    // Mapeo correcto con el backend (fechaCreacion -> fecha)
    @SerializedName("fechaCreacion")
    val fecha: String?,
    
    // Mapeo correcto con el backend (imagen -> imagenUrl)
    @SerializedName("imagen")
    val imagenUrl: String?
)
