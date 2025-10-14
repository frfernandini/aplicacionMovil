package com.example.aplicacion.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios ORDER BY id ASC")
    fun obtenerUsuarios(): Flow<List<UsuarioEntity>>

    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun obtenerUsuarioPorId(id: Int): UsuarioEntity?

    @Query("SELECT * FROM usuarios WHERE correo = :correo")
    suspend fun obtenerUsuarioPorCorreo(correo : String): UsuarioEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuario(usuario: UsuarioEntity): Long

    @Update
    suspend fun actualizar(usuario: UsuarioEntity)

    @Delete
    suspend fun eliminar(usuario: UsuarioEntity)

    @Query("DELETE FROM usuarios")
    suspend fun eliminarTodoUsuarios()

}