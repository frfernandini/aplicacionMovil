package com.example.aplicacion.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class ProductoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val precio: Int,
    val urlImagen: String,
    val categoria: String,

    //ESTADO PARA LA VALIDACION EN EL CARRITO
    val enCarrito: Boolean = false
)
