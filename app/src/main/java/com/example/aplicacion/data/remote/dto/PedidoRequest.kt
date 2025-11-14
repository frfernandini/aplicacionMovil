package com.example.aplicacion.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para enviar la solicitud de creación de un pedido al backend.
 */
data class PedidoRequest(
    @SerializedName("usuarioId") val usuarioId: String,
    @SerializedName("direccionEnvio") val direccionEnvio: String,
    @SerializedName("notas") val notas: String?,
    @SerializedName("items") val items: List<ItemCarritoRequest>
)

/**
 * DTO para representar cada ítem dentro de la solicitud de pedido.
 */
data class ItemCarritoRequest(
    @SerializedName("productoId") val productoId: Long,
    @SerializedName("cantidad") val cantidad: Int,
    // El backend espera un BigDecimal, pero desde Kotlin es más fácil enviar un Double.
    // Gson se encargará de la conversión si el backend está configurado para ello.
    @SerializedName("precioUnitario") val precioUnitario: Double
)
