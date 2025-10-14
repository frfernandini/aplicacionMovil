package com.example.aplicacion.model

data class ProductoUiState(
    val nombre: String = "",
    val descripcion: String = "",
    val precio: Double = 0.0,
    val urlImagen: String = "",
    val categoria: String = "",
    val errores: ProductoErrores = ProductoErrores() //CONTENEDOR DE ERRORES
)
