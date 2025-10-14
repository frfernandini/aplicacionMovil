package com.example.aplicacion.model.repository

import com.example.aplicacion.model.local.ProductoDAO
import com.example.aplicacion.model.local.ProductoEntity
import kotlinx.coroutines.flow.Flow

class ProductoRepository(private val dao: ProductoDAO) {

    fun obtenerProductos(): Flow<List<ProductoEntity>> = dao.mostrarTodos()

    suspend fun obtener(categoria: String) = dao.obtenerPorCategoria(categoria)

    suspend fun guardar(
        id: Int?,
        nombre: String?,
        descripcion: String?,
        precio: Double?,
        urlImagen: String?,
        categoria: String?,

    ){
        if (id == null || id == 0) {
            dao.insertar(
                ProductoEntity(
                    nombre = nombre.trim(),
                    descripcion = descripcion.trim(),
                    precio = precio,
                    urlImagen = urlImagen,
                    categoria = categoria.trim()
                )
            )
        } else {
            dao.actualizar(
                ProductoEntity(
                    id = id,
                    nombre = nombre.trim(),
                    descripcion = descripcion.trim(),
                    precio = precio,
                    urlImagen = urlImagen,
                    categoria = categoria.trim()
                )
            )

        }
    }

    suspend fun eliminar(producto: ProductoEntity) = dao.eliminar(producto)

    //PENDIENTE
    //REVISAR PRODUCTOdao
    //suspend fun eliminarTodos() = dao.eliminarTodos()
}