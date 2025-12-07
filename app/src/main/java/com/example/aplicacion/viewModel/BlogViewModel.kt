package com.example.aplicacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacion.data.remote.dto.BlogDto
import com.example.aplicacion.model.repository.BlogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BlogUiState(
    val blogs: List<BlogDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class BlogViewModel(private val repository: BlogRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(BlogUiState())
    val uiState = _uiState.asStateFlow()

    init {
        cargarBlogs()
    }

    fun cargarBlogs() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val blogs = repository.obtenerBlogs()
                if (blogs != null) {
                    _uiState.update { it.copy(blogs = blogs, isLoading = false) }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "No se pudieron cargar los blogs") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}