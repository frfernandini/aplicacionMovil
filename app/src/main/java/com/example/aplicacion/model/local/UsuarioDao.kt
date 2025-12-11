package com.example.aplicacion.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UsuarioDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(usuario: UsuarioEntity)

    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun get(id: Int): UsuarioEntity?

    // --- NUEVO: Obtener el último usuario guardado ---
    @Query("SELECT * FROM usuarios LIMIT 1")
    suspend fun getUltimoUsuario(): UsuarioEntity?
}
