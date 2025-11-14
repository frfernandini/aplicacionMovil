package com.example.aplicacion.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CategoriaDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("nombre") val nombre: String?
)

data class ProductoDto(
    @SerializedName("id") val id: Long,
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("descripcion") val descripcion: String?,
    @SerializedName("precio") val precio: Double?,
    @SerializedName("imagen") val imagen: String?,
    @SerializedName("categoria") val categoria: CategoriaDto?,
    @SerializedName("stock") val stock: Int?,
    @SerializedName("marca") val marca: String?,
    @SerializedName("destacado") val destacado: Boolean?,

    // --- CORREGIDO: Añadido para manejar la cantidad en el carrito ---
    val cantidad: Int = 1
)
