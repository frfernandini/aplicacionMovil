package com.example.aplicacion.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aplicacion.data.remote.RetrofitInstance
import com.example.aplicacion.model.local.AppDatabase
import com.example.aplicacion.model.repository.UsuarioRepository

class RegistroViewModelFactory(private val app: Application): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>):T{
        val db = AppDatabase.get(app)
        val apiService = RetrofitInstance.api
        val repo = UsuarioRepository(apiService,db.usuarioDao())
        return RegistroViewModel(repo) as T

    }
}