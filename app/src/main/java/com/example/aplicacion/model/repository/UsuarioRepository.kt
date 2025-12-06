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
import java.io.IOException // <-- IMPORTACIÓN AÑADIDA


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

    suspend fun guardarUsuario(usuario: UsuarioEntity) {
        usuarioDAO.insert(usuario)
    }

    suspend fun obtenerUsuario(id: Int): UsuarioEntity? {
        return usuarioDAO.get(id)
    }

    // --- NUEVA FUNCIÓN PARA SUBIR IMAGEN DE PERFIL ---
    suspend fun subirImagenPerfil(userId: String, imagenUri: Uri): Boolean {
        // 1. Convertir la URI a un archivo temporal
        val file = uriToFile(imagenUri)
        if (file == null) {
            return false // No se pudo crear el archivo
        }

        return try {
            // 2. Preparar el MultipartBody.Part
            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("imagen", file.name, requestFile)

            // 3. Llamar a la API
            val response = api.subirImagenPerfil(userId, body)

            if (response.isSuccessful && response.body() != null) {
                // 4. Actualizar la sesión con la nueva URL de la imagen
                SessionManager.userImageUrl = response.body()!!.imagenUrl
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            // 5. Limpiar el archivo temporal
            file.delete()
        }
    }

    // Función auxiliar para convertir una URI a un archivo
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
}