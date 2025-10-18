package com.example.aplicacion.model

import com.google.android.gms.maps.model.LatLng

data class Evento(
    val nombre: String,
    val fecha: String,
    val lugar: String,
    val imagenResId: Int, // Usamos un ID de recurso drawable
    val ubicacion: LatLng
)