package com.example.aplicacion.model

data class ProductoUiState(
    val id: Int? = null,
    val nombre: String = "",
    val descripcion: String = "",
    val precio: String = "",
    val urlImagen: String = "",
    val categoria: String = "",
    val enCarrito: Boolean = false,
    val errores: ProductoErrores = ProductoErrores(), //CONTENEDOR DE ERRORES
    val productoExitoso: Boolean = false
)
