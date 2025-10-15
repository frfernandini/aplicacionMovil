package com.example.aplicacion.model

import androidx.compose.ui.graphics.Color

data class Producto(
    val nombre: String,
    val imagen: Int,
    val calificacion: Float,
    val numResenas: Int,
    val precio: Double,
    val descuento: String? = null,
    val colorBorde: Color
)
