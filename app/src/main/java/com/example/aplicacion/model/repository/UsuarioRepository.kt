package com.example.aplicacion.model.repository

import android.content.Context
import android.net.Uri
import com.example.aplicacion.data.SessionManager
import com.example.aplicacion.data.remote.ApiService
import com.example.aplicacion.data.remote.dto.AuthResponse
import com.example.aplicacion.data.remote.dto.LoginRequest
import com.example.aplicacion.data.remote.dto.RegistroRequest
import com.example.aplicacion.model.local.UsuarioDAO
import com.example.aplicacion.model.local.UsuarioEntity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class UsuarioRepository(private val api: ApiService, private val usuarioDAO: UsuarioDAO, private val context: Context) {

    suspend fun registrarUsuarioRemoto(request: RegistroRequest): Response<AuthResponse> {
        return api.registrarUsuario(request)
    }

    suspend fun loginUsuario(request: LoginRequest): AuthResponse? {
        return try {
            val response = api.login(request)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun subirImagenPerfil(userId: String, imagenUri: Uri): Boolean {
        val file = uriToFile(imagenUri)
        if (file == null) {
            return false
        }

        return try {
            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("imagen", file.name, requestFile)

            val response = api.subirImagenPerfil(userId, body)

            if (response.isSuccessful && response.body() != null) {
                SessionManager.setUserImageUrl(response.body()!!.imagenUrl)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            file.delete()
        }
    }

    private fun uriToFile(uri: Uri): File? {
        return try {
            val contentResolver = context.contentResolver
            val tempFile = File.createTempFile("profile_pic", ".jpg", context.cacheDir)
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val outputStream = FileOutputStream(tempFile)

            inputStream.copyTo(outputStream)

            inputStream.close()
            outputStream.close()

            tempFile
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    // --- FUNCIONES DE PERSISTENCIA LOCAL ---
    suspend fun guardarUsuarioLocal(id: String, nombre: String, email: String) {
        // El ID de la entidad es un Int, pero el del backend es un String.
        // Usaremos un ID fijo (1) para sobreescribir siempre y recordar solo al último usuario.
        val entity = UsuarioEntity(
            id = 1, 
            nombre = nombre,
            correo = email,
            contrasena = "", // No guardamos la contraseña por seguridad
            direccion = ""
        )
        usuarioDAO.insert(entity)
    }

    suspend fun obtenerUltimoUsuarioLocal(): UsuarioEntity? {
        return usuarioDAO.getUltimoUsuario()
    }
}