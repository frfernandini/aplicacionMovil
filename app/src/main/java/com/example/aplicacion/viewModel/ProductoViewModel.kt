package com.example.aplicacion.viewModel

import androidx.lifecycle.ViewModel
import com.example.aplicacion.model.ProductoErrores
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

    fun onPrecioCharge(valor: String){
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

    fun validarProducto(): Boolean {
        val estadoActual = _estado.value
        val precioDouble = _estado.value.precio.toDoubleOrNull()?: 0.0

        val errores = ProductoErrores(
            nombre =
                if(estadoActual.nombre.isBlank())
                    "El Campo Es Obligatorio"
                else
                    null,
            descripcion =
                if(estadoActual.descripcion.isBlank())
                    "El Campo Es Obligatorio"
                else
                    null,
            precio =
                if(precioDouble <= 0)
                    "El valor debe ser mayor y distinto de cero"
                else
                    null,
            urlImagen =
                if(estadoActual.urlImagen.isBlank())
                    "La imagen es obligatoria"
                else
                    null,
            categoria =
                if(estadoActual.categoria.isBlank())
                    "El Campo Es Obligatorio"
                else
                    null,
        )

        val existenErrores = listOfNotNull(
            errores.nombre,
            errores.descripcion,
            errores.precio,
            errores.urlImagen,
            errores.categoria
        ).isNotEmpty()

        _estado.update { it.copy(errores = errores) }

        return !existenErrores
    }
}