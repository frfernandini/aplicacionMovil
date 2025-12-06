package com.example.aplicacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacion.data.remote.dto.EventoDto
import com.example.aplicacion.model.repository.EventoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EventosUiState(
    val eventos: List<EventoDto> = emptyList(),
    val isLoading: Boolean = false
)

class EventoViewModel(private val repo: EventoRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(EventosUiState())
    val uiState = _uiState.asStateFlow()

    init {
        cargarEventos()
    }

    fun cargarEventos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val resultado = repo.obtenerEventos()
            _uiState.update {
                it.copy(
                    eventos = resultado ?: emptyList(),
                    isLoading = false
                )
            }
        }
    }
}