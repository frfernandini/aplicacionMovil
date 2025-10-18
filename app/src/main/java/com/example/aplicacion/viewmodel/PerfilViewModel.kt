package com.example.aplicacion.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacion.model.PerfilUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.aplicacion.data.EstadoPreferenciasDataStore
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class PerfilViewModel(application: Application) : AndroidViewModel(application) {

    private val preferenciasUsuario = EstadoPreferenciasDataStore(application)

    private val _perfilState = MutableStateFlow(PerfilUiState())

    val perfilState: StateFlow<PerfilUiState> = _perfilState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                preferenciasUsuario.nombre,
                preferenciasUsuario.correo,
                preferenciasUsuario.imagenUri
            ) { nombre, correo, imagenUri ->
                _perfilState.update {
                    it.copy(
                        nombreUsuario = nombre ?: "Cargando...",
                        email = correo ?: "Cargando...",
                        imagenUri = imagenUri ?: ""
                    )
                }
            }.collect()
        }
    }

    fun onCerrarSesion(){
         viewModelScope.launch {
             preferenciasUsuario.borrarEstado()
                _perfilState.update { it.copy(logoutExitoso = true) }
         }
    }

    fun onNavegacionRealizada(){
        _perfilState.update { it.copy(logoutExitoso = false) }
    }
}