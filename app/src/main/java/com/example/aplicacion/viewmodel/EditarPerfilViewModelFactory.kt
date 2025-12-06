package com.example.aplicacion.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aplicacion.model.repository.UserPreferencesRepository

class EditarPerfilViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditarPerfilViewModel::class.java)) {
            // CAMBIO: Volvemos a inyectar el repositorio de preferencias locales
            val prefsRepository = UserPreferencesRepository(application)
            return EditarPerfilViewModel(application, prefsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}