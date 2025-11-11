package com.example.aplicacion.data.remote

import com.example.aplicacion.data.remote.dto.AuthResponse
import com.example.aplicacion.data.remote.dto.LoginRequest
import com.example.aplicacion.data.remote.dto.RegistroRequest

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("api/auth/registro")
    suspend fun registrarUsuario(@Body request: RegistroRequest): Response<AuthResponse>


    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest):Response<AuthResponse>
}