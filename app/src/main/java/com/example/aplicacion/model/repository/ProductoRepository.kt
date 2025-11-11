package com.example.aplicacion.model.repository

import com.example.aplicacion.data.remote.ApiService
import com.example.aplicacion.data.remote.dto.ProductoDto
import com.example.aplicacion.model.local.ProductoDAO
import com.example.aplicacion.model.local.ProductoEntity
import kotlinx.coroutines.flow.Flow

class ProductoRepository(private val apiService: ApiService, private val dao: ProductoDAO) {

    fun obtenerProductos(): Flow<List<ProductoEntity>> = dao.mostrarTodos()

    fun obtenerProdCarrito(): Flow<List<ProductoEntity>> = dao.obtenerCarrito()

    fun obtenerProdPorCategoria(categoria: String): Flow<List<ProductoEntity>> = dao.obtenerPorCategoria(categoria)

    suspend fun obtenerProductosRemotos(): List<ProductoDto>? {
        return try {
            val response = apiService.obtenerProductos()
            if (response.isSuccessful) {
                response.body()
            } else {
                null // El servidor devolvió un error (4xx, 5xx)
            }
        } catch (e: Exception) {
            null // Ocurrió un error de red (sin conexión, timeout, etc.)
        }
    }
    suspend fun agregarAlCarrito(producto: ProductoEntity){
        dao.actualizar(producto.copy(enCarrito = true))
    }

    suspend fun quitarDelCarrito(producto: ProductoEntity){
        dao.actualizar(producto.copy(enCarrito = false, cantidad = 1))
    }

    suspend fun actualizarCantidad(producto: ProductoEntity, cantidad: Int) {
        if (cantidad <= 0) {
            quitarDelCarrito(producto)
        } else {
            dao.actualizar(producto.copy(cantidad = cantidad, enCarrito = true))
        }
    }

    suspend fun vaciarCarro() {
        dao.vaciarCarrito()
    }

    suspend fun guardar(producto: ProductoEntity) {
        if (producto.id == 0) {
            dao.insertar(producto)
        } else {
            dao.actualizar(producto)
        }
    }

    suspend fun eliminar(producto: ProductoEntity) = dao.eliminar(producto)

}