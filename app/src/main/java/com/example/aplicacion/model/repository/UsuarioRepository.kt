package com.example.aplicacion.model.repository

import com.example.aplicacion.data.remote.dto.AuthResponse
import com.example.aplicacion.data.remote.dto.LoginRequest
import com.example.aplicacion.data.remote.dto.RegistroRequest
import com.example.aplicacion.model.local.UsuarioDao
import com.example.aplicacion.model.local.UsuarioEntity
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import com.example.aplicacion.data.remote.ApiService
class UsuarioRepository(private val apiService: ApiService,private val dao: UsuarioDao) {


    suspend fun registrarUsuarioRemoto(request: RegistroRequest): Response<AuthResponse>{
        return apiService.registrarUsuario(request)
    }

    suspend fun loginUsuario(request: LoginRequest): AuthResponse? {
        return try {
            val response = apiService.login(request)
            if (response.isSuccessful) {
                response.body()
            } else {
                // Puedes manejar errores específicos aquí (401, 403, etc.)
                null
            }
        } catch (e: Exception) {
            // Error de red, etc.
            null
        }
    }

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