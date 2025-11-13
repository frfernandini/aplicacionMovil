package com.example.aplicacion.data.remote.dto

import com.google.gson.annotations.SerializedName

// Esta es la estructura que tu backend realmente envía para la categoría
data class CategoriaDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("nombre") val nombre: String?
    // No es necesario añadir los otros campos de Categoria si no los usas
)

data class ProductoDto(
    @SerializedName("id") val id: Long,
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("descripcion") val descripcion: String?,
    @SerializedName("precio") val precio: Double?,
    @SerializedName("imagen") val imagen: String?,

    // --- CORREGIDO (DE VUELTA A OBJETO) ---
    // Ahora espera un objeto CategoriaDto, que coincide con la respuesta de /api/productos.
    @SerializedName("categoria") val categoria: CategoriaDto?,

    // Se mantienen los otros campos que observamos en el log.
    @SerializedName("stock") val stock: Int?,
    @SerializedName("marca") val marca: String?,
    @SerializedName("destacado") val destacado: Boolean?
)
