package com.example.aplicacion.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Este es un DTO específico para la respuesta del endpoint del carrito (/api/carrito/{id}).
 * Su única diferencia con ProductoDto es que 'categoria' es un String.
 */
data class CarritoProductoDto(
    @SerializedName("id") val id: Long,
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("descripcion") val descripcion: String?,
    @SerializedName("precio") val precio: Double?,
    @SerializedName("imagen") val imagen: String?,

    // En el carrito, la categoría es un String
    @SerializedName("categoria") val categoria: String?,

    @SerializedName("stock") val stock: Int?,
    @SerializedName("marca") val marca: String?,
    @SerializedName("destacado") val destacado: Boolean?,
    @SerializedName("cantidadEnCarrito") val cantidadEnCarrito: Int?
)
