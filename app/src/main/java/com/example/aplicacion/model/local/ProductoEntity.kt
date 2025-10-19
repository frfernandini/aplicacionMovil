package com.example.aplicacion.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class ProductoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagen: Int,
    val categoria: String,
    val cantidad: Int = 1,
    val enCarrito: Boolean = false
)
