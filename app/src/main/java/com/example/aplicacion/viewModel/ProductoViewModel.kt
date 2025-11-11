package com.example.aplicacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacion.data.remote.dto.ProductoDto
import com.example.aplicacion.model.ProductoErrores
import com.example.aplicacion.model.ProductoUiState
import com.example.aplicacion.model.local.ProductoEntity
import com.example.aplicacion.model.local.listaProductosEstaticos
import com.example.aplicacion.model.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductoViewModel(private val repo: ProductoRepository): ViewModel() {


    private val _productos = MutableStateFlow<List<ProductoDto>>(emptyList())
    val productos: StateFlow<List<ProductoDto>> = _productos.asStateFlow()

    private val _isLoading = MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    private val _estado = MutableStateFlow(ProductoUiState())
    val estado: StateFlow<ProductoUiState> = _estado.asStateFlow()

    private val _busquedaCategoria = MutableStateFlow("")
    val busquedaCategoria: StateFlow<String> = _busquedaCategoria

    private val _carritoIds = MutableStateFlow<Set<Long>>(emptySet())
    val carritoIds: StateFlow<Set<Long>> = _carritoIds.asStateFlow()

    val carrito: StateFlow<List<ProductoDto>> = productos.map { listaCompleta ->
        listaCompleta.filter { producto ->
            _carritoIds.value.contains(producto.id)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val totalCarrito: StateFlow<Double> = carrito
        .map { productosEnCarrito -> productosEnCarrito.sumOf { it.precio } } // Simplificado sin cantidad por ahora
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0.0
        )


    fun modificarCarrito(producto: ProductoDto) {
        _carritoIds.update { currentIds ->
            if (currentIds.contains(producto.id)) {
                currentIds - producto.id // Quita el ID del set
            } else {
                currentIds + producto.id // Añade el ID al set
            }
        }
    }
    fun cargarProductosRemotos() {
        // Evita recargas innecesarias si ya se está cargando
        if (_isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true
            val resultado =
                repo.obtenerProductosRemotos() // Llama a la función del repo que creamos

            if (resultado != null) {
                _productos.value = resultado // Éxito: Se actualiza el StateFlow
            } else {
                // Manejo de error: Podrías tener otro StateFlow para errores de red
                // Por ahora, la lista simplemente quedará vacía.
                // Log.e("ProductoViewModel", "Error al cargar productos del backend")
            }
            _isLoading.value = false
        }
    }


        //MAP PERMITE CAMBIAR O TRANSFORMAR LOS DATOS CON LOS QUE SON ENTREGADOS
        //EN ESTE CASO LA LISTA DE PRODUCTOS EN CARRITO
        //DE MODO QUE SEA DINAMICO PERMITIENDO EDITAR EL TOTAL DEL CARRITO CADA VES QUE SE MODIFIQUE EL CARRITO


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

        //CATEGORIA(LISTA ESTATICA)
        fun onCategoriaBusquedaChange(valor: String) {
            _busquedaCategoria.value = valor
        }

        fun limpiarFormProd() = run { _estado.value = ProductoUiState() }

        fun validarProducto(): Boolean {
            val precioDouble = _estado.value.precio.toDoubleOrNull() ?: 0.0

            val errores = ProductoErrores(
                nombre =
                    if (_estado.value.nombre.isBlank())
                        "El Campo Es Obligatorio"
                    else
                        null,
                descripcion =
                    if (_estado.value.descripcion.isBlank())
                        "El Campo Es Obligatorio"
                    else
                        null,
                precio =
                    if (precioDouble <= 0)
                        "El valor debe ser mayor y distinto de cero"
                    else
                        null,
                imagen =
                    if (_estado.value.imagen == null)
                        "La imagen es obligatoria"
                    else
                        null,
                categoria =
                    if (_estado.value.categoria.isBlank())
                        "El Campo Es Obligatorio"
                    else
                        null,
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
            val precioDouble = _estado.value.precio.toDoubleOrNull() ?: 0.0

            if (validarProducto()) {
                var producto = ProductoEntity(
                    id = _estado.value.id,
                    nombre = _estado.value.nombre,
                    descripcion = _estado.value.descripcion,
                    precio = precioDouble,
                    imagen = _estado.value.imagen,
                    categoria = _estado.value.categoria
                )

                //SI ESTA EDITANDO SE ASIGNA EL ID EXISTENTE

                viewModelScope.launch {
                    repo.guardar(producto)
                    _estado.update { it.copy(productoExitoso = true) }
                }
                limpiarFormProd()
                return true
            }
            return false
        }



        fun quitarDelCarrito(producto: ProductoDto) /*= viewModelScope.launch*/ {
            _carritoIds.update { currentIds ->
                currentIds - producto.id
            }
        }


        fun vaciarCarrito() {
            _carritoIds.value = emptySet()
        }


}