package com.example.aplicacion.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModel
class EditarPerfilViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditarPerfilViewModel::class.java)) {
            return EditarPerfilViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}