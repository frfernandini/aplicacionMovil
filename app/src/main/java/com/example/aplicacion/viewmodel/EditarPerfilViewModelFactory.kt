package com.example.aplicacion.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModel
import com.example.aplicacion.model.repository.UserPreferencesRepository

class EditarPerfilViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditarPerfilViewModel::class.java)) {
            // 1. Crear la dependencia del repositorio de preferencias
            val prefsRepository = UserPreferencesRepository(application)
            // 2. Pasarla al constructor del ViewModel
            return EditarPerfilViewModel(application, prefsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}