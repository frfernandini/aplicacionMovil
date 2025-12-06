package com.example.aplicacion.data.remote

import com.example.aplicacion.data.remote.dto.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("api/auth/registro")
    suspend fun registrarUsuario(@Body request: RegistroRequest): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @GET("/api/productos")
    suspend fun obtenerProductos(): Response<List<ProductoDto>>

    @GET("api/carrito/{usuarioId}")
    suspend fun obtenerCarrito(@Path("usuarioId") usuarioId: String): Response<List<CarritoProductoDto>>

    @POST("api/carrito/{usuarioId}/{productoId}")
    suspend fun agregarAlCarrito(
        @Path("usuarioId") usuarioId: String,
        @Path("productoId") productoId: Long
    ): Response<Unit>

    @DELETE("api/carrito/{usuarioId}/{productoId}")
    suspend fun quitarDelCarrito(
        @Path("usuarioId") usuarioId: String,
        @Path("productoId") productoId: Long
    ): Response<Unit>

    @DELETE("api/carritovacio/{usuarioId}")
    suspend fun vaciarCarrito(@Path("usuarioId") usuarioId: String): Response<Unit>

    @POST("api/pedidos")
    suspend fun crearPedido(@Body request: PedidoRequest): Response<Unit>

    // --- NUEVOS ENDPOINTS PARA CANTIDAD ---
    @POST("api/carrito/increase/{usuarioId}/{productoId}")
    suspend fun aumentarCantidad(
        @Path("usuarioId") usuarioId: String,
        @Path("productoId") productoId: Long
    ): Response<Unit>

    @POST("api/carrito/decrease/{usuarioId}/{productoId}")
    suspend fun disminuirCantidad(
        @Path("usuarioId") usuarioId: String,
        @Path("productoId") productoId: Long
    ): Response<Unit>

    // --- NUEVO ENDPOINT PARA EVENTOS ---
    @GET("api/eventos")
    suspend fun obtenerEventos(): Response<List<EventoDto>>

    // --- NUEVO ENDPOINT PARA SUBIR IMAGEN DE PERFIL ---
    @Multipart
    @PUT("api/usuarios/{id}/imagen")
    suspend fun subirImagenPerfil(
        @Path("id") usuarioId: String,
        @Part imagen: MultipartBody.Part
    ): Response<AuthResponse>
}
