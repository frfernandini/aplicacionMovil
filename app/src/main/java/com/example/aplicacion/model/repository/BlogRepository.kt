package com.example.aplicacion.model.repository

import com.example.aplicacion.data.remote.ApiService
import com.example.aplicacion.data.remote.dto.BlogDto

class BlogRepository(private val api: ApiService) {

    suspend fun obtenerBlogs(): List<BlogDto>? {
        return try {
            val response = api.obtenerBlogs()
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}