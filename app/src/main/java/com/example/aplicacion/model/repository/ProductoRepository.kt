package com.example.aplicacion.model.repository

import com.example.aplicacion.data.remote.ApiService
import com.example.aplicacion.data.remote.dto.CategoriaDto
import com.example.aplicacion.data.remote.dto.PedidoRequest
import com.example.aplicacion.data.remote.dto.ProductoDto
import com.example.aplicacion.model.local.ProductoDAO
import com.example.aplicacion.model.local.ProductoEntity
import kotlinx.coroutines.flow.Flow

class ProductoRepository(private val apiService: ApiService, private val dao: ProductoDAO) {

    fun obtenerProductos(): Flow<List<ProductoEntity>> = dao.mostrarTodos()
    fun obtenerProdPorCategoria(categoria: String): Flow<List<ProductoEntity>> = dao.obtenerPorCategoria(categoria)
    suspend fun guardar(producto: ProductoEntity) {
        if (producto.id == 0) dao.insertar(producto) else dao.actualizar(producto)
    }
    suspend fun eliminar(producto: ProductoEntity) = dao.eliminar(producto)

    suspend fun obtenerProductosRemotos(): List<ProductoDto>? {
        return try {
            val response = apiService.obtenerProductos()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun obtenerCarrito(usuarioId: String): List<ProductoDto>? {
        return try {
            val response = apiService.obtenerCarrito(usuarioId)
            if (response.isSuccessful) {
                response.body()?.map { carritoDto ->
                    ProductoDto(
                        id = carritoDto.id,
                        nombre = carritoDto.nombre,
                        descripcion = carritoDto.descripcion,
                        precio = carritoDto.precio,
                        imagen = carritoDto.imagen,
                        categoria = CategoriaDto(id = null, nombre = carritoDto.categoria),
                        stock = carritoDto.stock,
                        marca = carritoDto.marca,
                        destacado = carritoDto.destacado,
                        cantidad = carritoDto.cantidadEnCarrito ?: 1
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

    suspend fun crearPedido(request: PedidoRequest): Boolean {
        return try {
            apiService.crearPedido(request).isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    // --- NUEVAS FUNCIONES PARA CANTIDAD ---
    suspend fun aumentarCantidad(usuarioId: String, productoId: Long): Boolean {
        return try {
            apiService.aumentarCantidad(usuarioId, productoId).isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun disminuirCantidad(usuarioId: String, productoId: Long): Boolean {
        return try {
            apiService.disminuirCantidad(usuarioId, productoId).isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}
