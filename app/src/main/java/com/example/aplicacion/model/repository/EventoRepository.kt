package com.example.aplicacion.model.repository

import com.example.aplicacion.data.remote.ApiService
import com.example.aplicacion.data.remote.dto.EventoDto

class EventoRepository(private val api: ApiService) {

    suspend fun obtenerEventos(): List<EventoDto>? {
        try {
            val response = api.obtenerEventos()
            if (response.isSuccessful) {
                return response.body()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}