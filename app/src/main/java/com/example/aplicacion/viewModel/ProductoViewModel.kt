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

    //MAP PERMITE CAMBIAR O TRANSFORMAR LOS DATOS CON LOS QUE SON ENTREGADOS EN ESTE CASO LA LISTA DE
    //PRODUCTOS EN CARRITO
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

    fun onUrlImagenCharge(valor: String){
        _estado.update { it.copy(urlImagen = valor, errores = it.errores.copy(urlImagen = null)) }
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

    //VALIDAR EL FILTRADO DE PRODUCTOS DEL BUSCADOR DESDE EL COMPOSABLE
    val productosFiltrados = if (categoria.isBlank()) {
        productos
    } else {
        val filtrados = productos.filter {it.categoria.contains(categoria, ignoreCase = true)}

        if (filtrados.isEmpty()) productos else filtrados
    }

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

    fun cargarProdParaEditar(producto: ProductoEntity) {
        _estado.value = ProductoUiState(
            id = producto.id,
            nombre = producto.nombre,
            descripcion = producto.descripcion,
            precio = producto.precio.toString(),
            urlImagen = producto.urlImagen,
            categoria = producto.categoria
        )
    }


    fun guardarProducto(){
        val precioDouble = _estado.value.precio.toDoubleOrNull()?: 0.0

        if(validarProducto()){
            val productoNuevo = ProductoEntity(
                nombre = estado.value.nombre,
                descripcion = estado.value.descripcion,
                precio = precioDouble,
                urlImagen = estado.value.urlImagen,
                categoria = estado.value.categoria
            )

            viewModelScope.launch {
                repo.guardar(productoNuevo)
                _estado.update { it.copy(productoExitoso = true ) }
            }
        }
        limpiarFormProd()
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