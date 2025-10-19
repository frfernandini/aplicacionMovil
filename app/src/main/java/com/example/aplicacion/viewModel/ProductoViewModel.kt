package com.example.aplicacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacion.model.ProductoErrores
import com.example.aplicacion.model.ProductoUiState
import com.example.aplicacion.model.local.ProductoEntity
import com.example.aplicacion.model.local.listaProductosEstaticos
import com.example.aplicacion.model.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductoViewModel(private val repo: ProductoRepository): ViewModel() {

    // Catálogo estático
    private val _catalogo = MutableStateFlow(listaProductosEstaticos.toList())
    val catalogo: StateFlow<List<ProductoEntity>> = _catalogo


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
    //CARRITO DINAMICO
    /*val carrito: StateFlow<List<ProductoEntity>> =
        repo.obtenerProdCarrito().stateIn(
            scope =viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )*/

    //Carrito derivado de catalogo(ESTATICO)
    val carrito: StateFlow<List<ProductoEntity>> = _catalogo
        .map { lista -> lista.filter { it.enCarrito } }
        .stateIn(viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList())

    //Función para agregar/quitar del carrito(derivado del catalogo estatico)
    fun modificarCarrito(producto: ProductoEntity) {
        _catalogo.value = _catalogo.value.map {
            if (it == producto) {
                if (!it.enCarrito) {
                    it.copy(enCarrito = true, cantidad = 1) // Se agrega al carrito
                } else {
                    it.copy(enCarrito = false, cantidad = 0) // Se quita del carrito
                }
            } else it
        }
    }

    // Función para cambiar la categoría seleccionada
    fun seleccionarCategoria(categoria: String) {
        _busquedaCategoria.value = categoria
    }

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
    //CATEGORIA(LISTA ESTATICA)
    fun onCategoriaBusquedaChange(valor: String) {
        _busquedaCategoria.value = valor
    }

    fun limpiarFormProd() = run { _estado.value = ProductoUiState() }

    fun validarProducto(): Boolean {
        val precioDouble = _estado.value.precio.toDoubleOrNull()?: 0.0

        val errores = ProductoErrores(
            nombre =
                if(_estado.value.nombre.isBlank())
                    "El Campo Es Obligatorio"
                else
                    null,
            descripcion =
                if(_estado.value.descripcion.isBlank())
                    "El Campo Es Obligatorio"
                else
                    null,
            precio =
                if(precioDouble <= 0)
                    "El valor debe ser mayor y distinto de cero"
                else
                    null,
            imagen =
                if(_estado.value.imagen == null)
                    "La imagen es obligatoria"
                else
                    null,
            categoria =
                if(_estado.value.categoria.isBlank())
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

    //FUNCIONES DEL CARRITO
    //AJUSTAR LOS COMENTARIOS SI SE DESEA FUNCIONES DINAMICAS
    //LA SIGUIENTE FUNCION SOLO SE UTILIZA SI SE IMPLEMENTA EL FORMULARIO
    /*fun agregarAlCarrito(producto: ProductoEntity) = viewModelScope.launch {
        repo.agregarAlCarrito(producto)
    }*/

    fun quitarDelCarrito(producto: ProductoEntity) /*= viewModelScope.launch*/ {
        //repo.quitarDelCarrito(producto)
        _catalogo.value = _catalogo.value.map {
            if (it.nombre == producto.nombre) it.copy(enCarrito = false, cantidad = 1) else it
        }
    }

    fun cambiarCantidad(producto: ProductoEntity, cantidad: Int) /*= viewModelScope.launch*/ {
        //repo.actualizarCantidad(producto, cantidad)
        _catalogo.value = _catalogo.value.map {
            if (it == producto) it.copy(
                cantidad = if (cantidad < 0) 0 else cantidad,
                enCarrito = if (cantidad <= 0) false else true
            ) else it
        }
    }

    fun vaciarCarrito() /*= viewModelScope.launch*/ {
        //repo.vaciarCarro()
        _catalogo.value = _catalogo.value.map { it.copy(enCarrito = false, cantidad = 0) }
    }


    fun eliminarProducto(producto: ProductoEntity) = viewModelScope.launch { repo.eliminar(producto) }
}