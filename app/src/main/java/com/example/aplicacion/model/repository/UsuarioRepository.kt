package com.example.aplicacion.model.repository

import com.example.aplicacion.model.local.UsuarioDao
import com.example.aplicacion.model.local.UsuarioEntity
import kotlinx.coroutines.flow.Flow

class UsuarioRepository(private val dao: UsuarioDao) {
    fun observarUsuarios(): Flow<List<UsuarioEntity>> = dao.obtenerUsuarios()

    suspend fun obtener(id: Int) = dao.obtenerUsuarioPorId(id)

    suspend fun guardar(
        id: Int,
        nombre: String,
        correo: String,
        contrasena: String,
        direccion: String
    ){
        if(id == null || id == 0){
            dao.insertarUsuario(
                UsuarioEntity(
                    nombre = nombre,
                    correo = correo,
                    contrasena = contrasena,
                    direccion = direccion

                )
            )
        }else{
            dao.actualizarUsuario(
                UsuarioEntity(
                    id = id,
                    nombre = nombre,
                    correo = correo,
                    contrasena = contrasena,
                    direccion = direccion
                )
            )
        }
    }

    suspend fun eliminar(usuario: UsuarioEntity) = dao.eliminarUsuario(usuario)
    suspend fun eliminarTodo() = dao.eliminarTodo()

}