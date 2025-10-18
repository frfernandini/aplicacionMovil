package com.example.aplicacion.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacion.model.ProductoErrores
import com.example.aplicacion.model.ProductoUiState
import com.example.aplicacion.model.local.ProductoEntity
import com.example.aplicacion.model.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductoViewModel(private val repo: ProductoRepository): ViewModel() {

    private val _estado = MutableStateFlow(ProductoUiState())
    //ESTADO EXPUESTO PARA LA UI
    val estado: StateFlow<ProductoUiState> = _estado

    private val _busquedaCategoria = MutableStateFlow("")
    val busquedaCategoria: StateFlow<String> = _busquedaCategoria


    val productos: StateFlow<List<ProductoEntity>> =
        repo.obtenerProductos().stateIn(
            scope =viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
    //ESTO NOS PERMITE VISUALIZAR TODOS LOS PRODUCTOS AGREGADOS AL CARRITO MEDIANTE LA VARIABLE enCarrito
    //DECLARADA ANTERIORMENTE
    val carrito: StateFlow<List<ProductoEntity>> =
        repo.obtenerProdCarrito().stateIn(
            scope =viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    //MAP PERMITE CAMBIAR O TRANSFORMAR LOS DATOS CON LOS QUE SON ENTREGADOS
    // EN ESTE CASO LA LISTA DE PRODUCTOS EN CARRITO
    //DE MODO QUE SEA DINAMICO PERMITIENDO EDITAR EL TOTAL DEL CARRITO CADA VES QUE SE MODIFIQUE EL CARRITO
    val totalCarrito: StateFlow<Double> = carrito
        .map { productos -> productos.sumOf { it.precio * it.cantidad }}
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0.0
        )

    fun onNombreCharge(valor: String){
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }

    fun onDescripcionCharge(valor: String){
        _estado.update { it.copy(descripcion = valor, errores = it.errores.copy(descripcion = null)) }
    }

    fun onPrecioCharge(valor: String){
        _estado.update { it.copy(precio = valor, errores = it.errores.copy(precio = null)) }
    }

    fun onImagenCharge(valor: Int){
        _estado.update { it.copy(imagen = valor, errores = it.errores.copy(imagen = null)) }
    }

    fun onCategoriaCharge(valor: String){
        _estado.update { it.copy(categoria = valor, errores = it.errores.copy(categoria = null)) }
    }

    fun onEnCarritoCharge(valor: Boolean){
        _estado.update { it.copy(enCarrito = valor) }
    }

    fun onCategoriaBusquedaChange(valor: String) {
        _busquedaCategoria.value = valor
    }

    //VALIDAR EL FILTRADO DE PRODUCTOS DEL BUSCADOR DESDE EL COMPOSE
    /*val productosFiltrados = if (categoria.isBlank()) {
        productos
    } else {
        val filtrados = productos.filter {it.categoria.contains(categoria, ignoreCase = true)}

        if (filtrados.isEmpty()) productos else filtrados
    }*/

    fun limpiarFormProd() = run { _estado.value = ProductoUiState() }

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
            imagen =
                if(estadoActual.imagen == null)
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
            // Crear producto base
            var producto = ProductoEntity(
                nombre = estado.value.nombre,
                descripcion = estado.value.descripcion,
                precio = precioDouble,
                imagen = estado.value.imagen,
                categoria = estado.value.categoria
            )

            // Si estamos editando, asignamos el id existente
            _estado.value.id.takeIf { it != 0 }?.let { id ->
                producto = producto.copy(id = id)
            }

            viewModelScope.launch {
                repo.guardar(producto)
                _estado.update { it.copy(productoExitoso = true) }
            }
            limpiarFormProd()
            return true
        }
        return false
    }

    //FUNCIONES DEL CARRITO
    fun agregarAlCarrito(producto: ProductoEntity) = viewModelScope.launch {
        repo.agregarAlCarrito(producto)
    }

    fun quitarDelCarrito(producto: ProductoEntity) = viewModelScope.launch {
        repo.quitarDelCarrito(producto)
    }

    fun cambiarCantidad(producto: ProductoEntity, cantidad: Int) = viewModelScope.launch {
        repo.actualizarCantidad(producto, cantidad)
    }

    fun eliminarProducto(producto: ProductoEntity) = viewModelScope.launch { repo.eliminar(producto) }
}