package com.example.aplicacion.model.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDAO {

    @Query("SELECT * FROM productos ORDER BY nombre ASC")
    fun mostrarTodos(): Flow<List<ProductoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(producto: ProductoEntity)

    @Update
    suspend fun actualizar(producto: ProductoEntity)

    @Delete
    suspend fun eliminar(producto: ProductoEntity)

    //VERIFICARLO
    //@Query("DELETE FROM productos")
    //suspend fun eliminarTodos()
}