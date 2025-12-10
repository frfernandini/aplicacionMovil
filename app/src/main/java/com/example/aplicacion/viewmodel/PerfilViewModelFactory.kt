package com.example.aplicacion.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PerfilViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PerfilViewModel::class.java)) {
            // ARREGLO: El ViewModel ya no necesita el repositorio, solo la aplicación.
            return PerfilViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}