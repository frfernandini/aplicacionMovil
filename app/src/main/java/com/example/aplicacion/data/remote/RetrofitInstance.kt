package com.example.aplicacion.data.remote

import com.example.aplicacion.data.SessionManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://192.168.100.14:8080/"

    // --- INTERCEPTOR DE AUTENTICACIÓN ---
    private val authInterceptor = Interceptor { chain ->
        val requestBuilder = chain.request().newBuilder()

        SessionManager.authToken?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        chain.proceed(requestBuilder.build())
    }

    private val client by lazy {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        OkHttpClient.Builder()
            // --- CORREGIDO: El interceptor de Auth va PRIMERO ---
            .addInterceptor(authInterceptor)
            // --- Y el de logging va DESPUÉS para registrar la petición ya modificada ---
            .addInterceptor(logging)
            .build()
    }

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
