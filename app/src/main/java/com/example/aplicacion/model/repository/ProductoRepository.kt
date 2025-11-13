package com.example.aplicacion.model.repository

import com.example.aplicacion.data.remote.ApiService
import com.example.aplicacion.data.remote.dto.CategoriaDto
import com.example.aplicacion.data.remote.dto.ProductoDto
import com.example.aplicacion.model.local.ProductoDAO
import com.example.aplicacion.model.local.ProductoEntity
import kotlinx.coroutines.flow.Flow

class ProductoRepository(private val apiService: ApiService, private val dao: ProductoDAO) {

    // --- Local DB Operations (for admin features, etc.) ---
    fun obtenerProductos(): Flow<List<ProductoEntity>> = dao.mostrarTodos()
    fun obtenerProdPorCategoria(categoria: String): Flow<List<ProductoEntity>> = dao.obtenerPorCategoria(categoria)
    suspend fun guardar(producto: ProductoEntity) {
        if (producto.id == 0) dao.insertar(producto) else dao.actualizar(producto)
    }
    suspend fun eliminar(producto: ProductoEntity) = dao.eliminar(producto)

    // --- Remote Product Operations ---
    suspend fun obtenerProductosRemotos(): List<ProductoDto>? {
        return try {
            val response = apiService.obtenerProductos()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

    // --- Cart Logic (Backend Driven) ---

    // ¡AQUÍ ESTÁ LA TRADUCCIÓN!
    suspend fun obtenerCarrito(usuarioId: String): List<ProductoDto>? {
        return try {
            val response = apiService.obtenerCarrito(usuarioId)
            if (response.isSuccessful) {
                // Mapea la respuesta del carrito (CarritoProductoDto) al DTO estándar (ProductoDto)
                response.body()?.map { carritoDto ->
                    ProductoDto(
                        id = carritoDto.id,
                        nombre = carritoDto.nombre,
                        descripcion = carritoDto.descripcion,
                        precio = carritoDto.precio,
                        imagen = carritoDto.imagen,
                        // Crea el objeto CategoriaDto a partir del String
                        categoria = CategoriaDto(id = null, nombre = carritoDto.categoria),
                        stock = carritoDto.stock,
                        marca = carritoDto.marca,
                        destacado = carritoDto.destacado
                    )
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun agregarAlCarrito(usuarioId: String, productoId: Long): Boolean {
        return try {
            apiService.agregarAlCarrito(usuarioId, productoId).isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun quitarDelCarrito(usuarioId: String, productoId: Long): Boolean {
        return try {
            apiService.quitarDelCarrito(usuarioId, productoId).isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun vaciarCarrito(usuarioId: String): Boolean {
        return try {
            apiService.vaciarCarrito(usuarioId).isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}
