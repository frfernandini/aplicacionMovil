package com.example.aplicacion.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("token") val token: String,
    @SerializedName("id") val id: String,       // <-- ADDED
    @SerializedName("nombre") val nombre: String   // <-- ADDED
)
