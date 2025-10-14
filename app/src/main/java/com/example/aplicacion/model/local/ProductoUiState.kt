package com.example.aplicacion.model.local

data class ProductoUiState(
    val nombre: String = "",
    val descripcion: String = "",
    val precio: Int = 0,
    val urlImagen: String = "",
    val categoria: String = "",
    val errores: ProductoErrores = ProductoErrores() //CONTENEDOR DE ERRORES
)
