package com.example.aplicacion.model.repository

import com.example.aplicacion.model.local.UsuarioDao
import com.example.aplicacion.model.local.UsuarioEntity
import kotlinx.coroutines.flow.Flow

class UsuarioRepository(private val dao: UsuarioDao) {
    fun observarUsuarios(): Flow<List<UsuarioEntity>> = dao.obtenerUsuarios()

    suspend fun obtener(id: Int): UsuarioEntity? = dao.obtenerUsuarioPorId(id)

    suspend fun obtenerPorCorreo(correo: String): UsuarioEntity? = dao.obtenerUsuarioPorCorreo(correo)

    suspend fun guardar(usuario: UsuarioEntity) {
        if (usuario.id == 0) {
            dao.insertarUsuario(usuario)
        } else {
            dao.actualizar(usuario)
        }
    }

    suspend fun eliminar(usuario: UsuarioEntity) = dao.eliminar(usuario)
    suspend fun eliminarTodo() = dao.eliminarTodoUsuarios()

}