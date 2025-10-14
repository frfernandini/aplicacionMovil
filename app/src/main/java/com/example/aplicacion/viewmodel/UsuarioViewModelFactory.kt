package com.example.aplicacion.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aplicacion.model.local.AppDatabase
import com.example.aplicacion.model.repository.UsuarioRepository

class UsuarioViewModelFactory(private val app: Application): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>):T{
        val db = AppDatabase.get(app)
        val repo = UsuarioRepository(db.usuarioDao())
        return UsuarioViewModel(repo) as T

    }
}