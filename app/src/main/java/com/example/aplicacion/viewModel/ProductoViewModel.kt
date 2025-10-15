package com.example.aplicacion.viewModel

import androidx.lifecycle.ViewModel
import com.example.aplicacion.model.ProductoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ProductoViewModel: ViewModel() {

    private val _estado = MutableStateFlow(ProductoUiState())

    //ESTADO EXPUESTO PARA LA UI
    val estado: StateFlow<ProductoUiState> = _estado

    fun onNombreCharge(valor: String){
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }

    fun onDescripcionCharge(valor: String){
        _estado.update { it.copy(descripcion = valor, errores = it.errores.copy(descripcion = null)) }
    }

    fun onPrecioCharge(valor: Double){
        _estado.update { it.copy(precio = valor, errores = it.errores.copy(precio = null)) }
    }

    fun onUrlImagenCharge(valor: String){
        _estado.update { it.copy(urlImagen = valor, errores = it.errores.copy(urlImagen = null)) }
    }

    fun onCategoriaCharge(valor: String){
        _estado.update { it.copy(categoria = valor, errores = it.errores.copy(categoria = null)) }
    }

    fun onEnCarritoCharge(valor: Boolean){
        _estado.update { it.copy(enCarrito = valor) }
    }
}