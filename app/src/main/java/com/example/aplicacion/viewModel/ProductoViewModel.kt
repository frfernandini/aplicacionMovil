package com.example.aplicacion.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacion.data.remote.dto.ProductoDto
import com.example.aplicacion.model.ProductoErrores
import com.example.aplicacion.model.ProductoUiState
import com.example.aplicacion.model.local.ProductoEntity
import com.example.aplicacion.model.repository.ProductoRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductoViewModel(private val repo: ProductoRepository) : ViewModel() {


    private val _productos = MutableStateFlow<List<ProductoDto>>(emptyList())
    val productos: StateFlow<List<ProductoDto>> = _productos.asStateFlow()

    private val _carrito = MutableStateFlow<List<ProductoDto>>(emptyList())
    val carrito: StateFlow<List<ProductoDto>> = _carrito.asStateFlow()

    private val _totalCarrito = MutableStateFlow(0.0)
    val totalCarrito: StateFlow<Double> = _totalCarrito.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _estado = MutableStateFlow(ProductoUiState())
    val estado: StateFlow<ProductoUiState> = _estado.asStateFlow()

    private val _busquedaCategoria = MutableStateFlow("")
    val busquedaCategoria: StateFlow<String> = _busquedaCategoria


    private var usuarioId: String? = null

    init {
        Log.d("ViewModelLifecycle", "ProductoViewModel ¡HA SIDO CREADO!")
        cargarProductosRemotos()
    }


    fun setUsuarioId(id: String) {
        usuarioId = id
        cargarCarrito()
    }

    private fun cargarCarrito() {
        usuarioId?.let {
            viewModelScope.launch {
                _isLoading.value = true
                val carritoResult = repo.obtenerCarrito(it)
                if (carritoResult != null) {
                    _carrito.value = carritoResult
                    calcularTotalCarrito()
                }
                _isLoading.value = false
            }
        }
    }

    fun agregarAlCarrito(producto: ProductoDto) {
        usuarioId?.let { uId ->
            viewModelScope.launch {
                val success = repo.agregarAlCarrito(uId, producto.id)
                if (success) {
                    cargarCarrito() // Refresh cart from backend
                }
            }
        }
    }

    fun quitarDelCarrito(producto: ProductoDto) {
        usuarioId?.let { uId ->
            viewModelScope.launch {
                val success = repo.quitarDelCarrito(uId, producto.id)
                if (success) {
                    cargarCarrito() // Refresh cart from backend
                }
            }
        }
    }

    fun vaciarCarrito() {
        usuarioId?.let { uId ->
            viewModelScope.launch {
                val success = repo.vaciarCarrito(uId)
                if (success) {
                    _carrito.value = emptyList()
                    _totalCarrito.value = 0.0
                }
            }
        }
    }

    private fun calcularTotalCarrito() {
        _totalCarrito.value = _carrito.value.sumOf { it.precio ?: 0.0 }
    }

    // --- PRODUCT LOADING ---

    fun cargarProductosRemotos() {
        if (_isLoading.value) return
        Log.d("ViewModelLifecycle", "cargarProductosRemotos() FUE LLAMADO")
        viewModelScope.launch {
            _isLoading.value = true
            val resultado = repo.obtenerProductosRemotos()
            Log.d("ViewModelLifecycle", "Resultado de la API: ${resultado?.size ?: "null"} productos")

            if (resultado != null) {
                _productos.value = resultado
            } else {
                Log.e("ViewModelLifecycle", "Error al cargar productos del backend o lista vacía.")
            }
            _isLoading.value = false
        }
    }



    fun onNombreCharge(valor: String) {
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }

    fun onDescripcionCharge(valor: String) {
        _estado.update {
            it.copy(
                descripcion = valor,
                errores = it.errores.copy(descripcion = null)
            )
        }
    }

    fun onPrecioCharge(valor: String) {
        _estado.update { it.copy(precio = valor, errores = it.errores.copy(precio = null)) }
    }

    fun onImagenCharge(valor: Int) {
        _estado.update { it.copy(imagen = valor, errores = it.errores.copy(imagen = null)) }
    }

    fun onCategoriaCharge(valor: String) {
        _estado.update {
            it.copy(
                categoria = valor,
                errores = it.errores.copy(categoria = null)
            )
        }
    }

    fun onEnCarritoCharge(valor: Boolean) {
        _estado.update { it.copy(enCarrito = valor) }
    }

    fun onCategoriaBusquedaChange(valor: String) {
        _busquedaCategoria.value = valor
    }

    fun limpiarFormProd() = run { _estado.value = ProductoUiState() }

    fun validarProducto(): Boolean {
        val precioDouble = _estado.value.precio.toDoubleOrNull() ?: 0.0

        val errores = ProductoErrores(
            nombre = if (_estado.value.nombre.isBlank()) "El Campo Es Obligatorio" else null,
            descripcion = if (_estado.value.descripcion.isBlank()) "El Campo Es Obligatorio" else null,
            precio = if (precioDouble <= 0) "El valor debe ser mayor y distinto de cero" else null,
            imagen = if (_estado.value.imagen == 0) "La imagen es obligatoria" else null, // Ajustado a Int=0 como inválido
            categoria = if (_estado.value.categoria.isBlank()) "El Campo Es Obligatorio" else null,
        )

        val existenErrores = listOfNotNull(
            errores.nombre,
            errores.descripcion,
            errores.precio,
            errores.imagen,
            errores.categoria
        ).isNotEmpty()

        _estado.update { it.copy(errores = errores) }
        return !existenErrores
    }

    fun cargarProdParaEditar(producto: ProductoEntity) {
        _estado.value = ProductoUiState(
            id = producto.id,
            nombre = producto.nombre,
            descripcion = producto.descripcion,
            precio = producto.precio.toString(),
            imagen = producto.imagen,
            categoria = producto.categoria
        )
    }

    fun guardarProducto(): Boolean {
        if (validarProducto()) {
            val producto = ProductoEntity(
                id = _estado.value.id,
                nombre = _estado.value.nombre,
                descripcion = _estado.value.descripcion,
                precio = _estado.value.precio.toDoubleOrNull() ?: 0.0,
                imagen = _estado.value.imagen,
                categoria = _estado.value.categoria
            )
            viewModelScope.launch {
                repo.guardar(producto)
                _estado.update { it.copy(productoExitoso = true) }
            }
            limpiarFormProd()
            return true
        }
        return false
    }
}