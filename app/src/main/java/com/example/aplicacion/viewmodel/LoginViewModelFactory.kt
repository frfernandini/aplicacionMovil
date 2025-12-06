package com.example.aplicacion.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aplicacion.model.local.AppDatabase
import com.example.aplicacion.model.repository.UsuarioRepository
import com.example.aplicacion.data.remote.RetrofitInstance
class LoginViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass : Class<T> ):T {
        val db = AppDatabase.get(app)
        val apiService = RetrofitInstance.api
        // ARREGLO: Pasamos el contexto al constructor del repositorio
        val repo = UsuarioRepository(apiService, db.usuarioDao(), app)
        return LoginViewModel(app,repo) as T

    }
}