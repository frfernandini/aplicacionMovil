package com.example.aplicacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aplicacion.data.SessionManager
import com.example.aplicacion.data.remote.dto.ItemCarritoRequest
import com.example.aplicacion.data.remote.dto.PedidoRequest
import com.example.aplicacion.data.remote.dto.ProductoDto
import com.example.aplicacion.model.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CheckoutUiState(
    val direccion: String = "",
    val notas: String = "",
    val errorDireccion: String? = null,
    val pedidoExitoso: Boolean = false,
    val isLoading: Boolean = false,
    val errorGeneral: String? = null
)

class CheckoutViewModel(private val repository: ProductoRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState = _uiState.asStateFlow()

    fun onDireccionChange(direccion: String) {
        _uiState.update { it.copy(direccion = direccion, errorDireccion = null) }
    }

    fun onNotasChange(notas: String) {
        _uiState.update { it.copy(notas = notas) }
    }

    // --- CORREGIDO: Ahora recibe el carrito directamente ---
    fun crearPedido(carrito: List<ProductoDto>) {
        // Obtenemos el userId desde la fuente de la verdad: el SessionManager
        val userId = SessionManager.userId

        if (userId == null) {
            _uiState.update { it.copy(errorGeneral = "Error: Sesión no encontrada. Por favor, reinicia la aplicación.") }
            return
        }

        if (!validarFormulario()) return

        _uiState.update { it.copy(isLoading = true, errorGeneral = null) }

        viewModelScope.launch {
            val itemsRequest = carrito.map {
                ItemCarritoRequest(
                    productoId = it.id,
                    cantidad = it.cantidad,
                    precioUnitario = it.precio ?: 0.0
                )
            }

            val pedidoRequest = PedidoRequest(
                usuarioId = userId, // <-- Usa el ID del SessionManager
                direccionEnvio = _uiState.value.direccion,
                notas = _uiState.value.notas.takeIf { it.isNotBlank() },
                items = itemsRequest
            )

            val success = repository.crearPedido(pedidoRequest)

            if (success) {
                _uiState.update { it.copy(isLoading = false, pedidoExitoso = true) }
            } else {
                _uiState.update { it.copy(isLoading = false, errorGeneral = "No se pudo crear el pedido. Inténtalo de nuevo.") }
            }
        }
    }

    private fun validarFormulario(): Boolean {
        val direccionValida = _uiState.value.direccion.isNotBlank()
        if (!direccionValida) {
            _uiState.update { it.copy(errorDireccion = "La dirección de envío es obligatoria") }
        }
        return direccionValida
    }

    fun onNavegacionRealizada() {
        _uiState.update { it.copy(pedidoExitoso = false) }
    }
}

class CheckoutViewModelFactory(private val repository: ProductoRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckoutViewModel::class.java)) {
            return CheckoutViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
