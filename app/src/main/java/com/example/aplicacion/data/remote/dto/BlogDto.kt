package com.example.aplicacion.data.remote.dto

import com.google.gson.annotations.SerializedName

data class BlogDto(
    val id: Long,
    // ARREGLO: Todos los campos String ahora son nullables para evitar crashes por mala data
    val titulo: String?, 
    val contenido: String?,
    val autor: String?,
    
    @SerializedName("fechaCreacion")
    val fecha: String?,
    
    @SerializedName("imagen")
    val imagenUrl: String?
)
