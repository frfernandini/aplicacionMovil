package com.example.aplicacion.model.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDAO {

    @Query("SELECT * FROM productos ORDER BY nombre ASC")
    fun mostrarTodos(): Flow<List<ProductoEntity>>

    @Query("SELECT * FROM productos WHERE categoria = categoria")
    suspend fun obtenerPorCategoria(categoria: String): ProductoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(producto: ProductoEntity): Long

    @Update
    suspend fun actualizar(producto: ProductoEntity)

    @Delete
    suspend fun eliminar(producto: ProductoEntity)

    //VERIFICARLO
    //@Query("DELETE FROM productos")
    //suspend fun eliminarTodos()
}