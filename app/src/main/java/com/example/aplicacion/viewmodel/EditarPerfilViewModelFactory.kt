package com.example.aplicacion.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aplicacion.data.remote.RetrofitInstance
import com.example.aplicacion.model.local.AppDatabase
import com.example.aplicacion.model.repository.UsuarioRepository

class EditarPerfilViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditarPerfilViewModel::class.java)) {
            // ARREGLO: Creamos las dependencias para UsuarioRepository (Red)
            val db = AppDatabase.get(application)
            val apiService = RetrofitInstance.api
            val repository = UsuarioRepository(apiService, db.usuarioDao(), application)
            
            // Pasamos el repositorio de red al ViewModel
            return EditarPerfilViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}