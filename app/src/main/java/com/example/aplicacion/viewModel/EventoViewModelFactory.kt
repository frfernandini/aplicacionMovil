package com.example.aplicacion.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aplicacion.data.remote.RetrofitInstance
import com.example.aplicacion.model.repository.EventoRepository

class EventoViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val api = RetrofitInstance.api
        val repo = EventoRepository(api)
        return EventoViewModel(repo) as T
    }
}