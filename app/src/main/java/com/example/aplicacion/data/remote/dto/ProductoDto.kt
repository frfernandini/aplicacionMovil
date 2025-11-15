package com.example.aplicacion.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CategoriaDto(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("nombre") val nombre: String? = null
)

data class ProductoDto(
    @SerializedName("id") val id: Long,
    @SerializedName("nombre") val nombre: String? = null,
    @SerializedName("descripcion") val descripcion: String? = null,
    @SerializedName("precio") val precio: Double? = null,
    @SerializedName("imagen") val imagen: String? = null,
    @SerializedName("categoria") val categoria: CategoriaDto? = null,
    @SerializedName("stock") val stock: Int? = null,
    @SerializedName("marca") val marca: String? = null,
    @SerializedName("destacado") val destacado: Boolean? = null,

    // --- CORREGIDO: Añadido para manejar la cantidad en el carrito ---
    val cantidad: Int = 1
)
