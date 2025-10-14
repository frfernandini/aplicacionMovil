package com.example.aplicacion.model.repository

import com.example.aplicacion.model.local.ProductoDAO
import com.example.aplicacion.model.local.ProductoEntity
import kotlinx.coroutines.flow.Flow

class ProductoRepository(private val dao: ProductoDAO) {

    fun obtenerProductos(): Flow<List<ProductoEntity>> = dao.mostrarTodos()

    fun obtenerProdCarrito(): Flow<List<ProductoEntity>> = dao.obtenerCarrito()

    suspend fun obtener(categoria: String) = dao.obtenerPorCategoria(categoria)

    //FUNCIONES QUE SE UTILIZARAN POSTERIORMENTE PARA AGREGAR PRODUCTOS Y VICEVERSA
    suspend fun agregarAlCarrito(producto: ProductoEntity){
        dao.actualizar(producto.copy(enCarrito = true))
    }

    suspend fun quitarDelCarrito(producto: ProductoEntity){
        dao.actualizar(producto.copy(enCarrito = false))
    }


    suspend fun guardar(producto: ProductoEntity){
        if (producto.id == null || producto.id == 0) {
            dao.insertar(producto)
        } else {
            dao.actualizar(producto)

        }
    }

    suspend fun eliminar(producto: ProductoEntity) = dao.eliminar(producto)

    //PENDIENTE
    //REVISAR PRODUCTOdao
    //suspend fun eliminarTodos() = dao.eliminarTodos()
}