package com.example.aplicacion.data.remote

import com.example.aplicacion.data.SessionManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    // url beanstalk:http://levelup.us-east-1.elasticbeanstalk.com/
    //url local:http://10.0.2.2:5000/
    private const val BASE_URL = "http://levelup.us-east-1.elasticbeanstalk.com"

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
            .addInterceptor(authInterceptor)
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