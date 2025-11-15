package com.example.aplicacion

import com.example.aplicacion.data.remote.dto.ProductoDto
import com.example.aplicacion.model.repository.ProductoRepository
import com.example.aplicacion.viewmodel.ProductoViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class ProductoViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val mockRepo = mockk<ProductoRepository>(relaxed = true)
    private lateinit var viewModel: ProductoViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ProductoViewModel(mockRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `cargarProductosRemotos gestiona el estado isLoading correctamente`() = runTest {
        // Arrange
        val deferred = CompletableDeferred<List<ProductoDto>>()
        coEvery { mockRepo.obtenerProductosRemotos() } coAnswers { deferred.await() }

        // Act
        viewModel.cargarProductosRemotos()

        // Assert: isLoading should be true after the coroutine starts but before it completes.
        testDispatcher.scheduler.runCurrent()
        assertTrue("isLoading should be true while fetching data", viewModel.isLoading.value)

        // Act: Complete the coroutine
        deferred.complete(emptyList())
        
        // Assert: Check the final loading state
        testDispatcher.scheduler.advanceUntilIdle()
        assertFalse("isLoading should be false after fetching data", viewModel.isLoading.value)
    }


    // --- Test de Carrito ---

    @Test
    fun `vaciarCarrito borra los productos y resetea el total`() = runTest {
        viewModel.setUsuarioId("test-user")
        coEvery { mockRepo.vaciarCarrito("test-user") } returns true

        viewModel.vaciarCarrito()
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { mockRepo.vaciarCarrito("test-user") }
        assertTrue(viewModel.carrito.value.isEmpty())
        assertEquals(0.0, viewModel.totalCarrito.value, 0.0)
    }

    @Test
    fun `aumentarCantidad llama al repositorio`() = runTest {
        viewModel.setUsuarioId("test-user")
        val producto = ProductoDto(id = 1L, nombre = "Test 1", precio = 10.0, cantidad = 2)

        viewModel.aumentarCantidad(producto)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { mockRepo.aumentarCantidad("test-user", 1L) }
    }

    @Test
    fun `disminuirCantidad con cantidad mayor a 1 llama al repositorio`() = runTest {
        viewModel.setUsuarioId("test-user")
        val producto = ProductoDto(id = 1L, nombre = "Test 1", precio = 10.0, cantidad = 3)

        viewModel.disminuirCantidad(producto)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { mockRepo.disminuirCantidad("test-user", 1L) }
        coVerify(exactly = 0) { mockRepo.quitarDelCarrito(any(), any()) } // No debe quitarlo
    }


    @Test
    fun `agregarAlCarrito refresca el carrito`() = runTest {
        viewModel.setUsuarioId("test-user")
        val producto = ProductoDto(id = 1L, nombre = "Test 1", precio = 10.0)
        coEvery { mockRepo.agregarAlCarrito("test-user", 1L) } returns true
        coEvery { mockRepo.obtenerCarrito("test-user") } returns listOf(producto)

        viewModel.agregarAlCarrito(producto)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { mockRepo.obtenerCarrito("test-user") }
        assertEquals(1, viewModel.carrito.value.size)
        assertEquals(10.0, viewModel.totalCarrito.value, 0.0)
    }

    @Test
    fun `disminuirCantidad con cantidad 1_quitar del carrito`() = runTest {
        viewModel.setUsuarioId("test-user")
        val producto = ProductoDto(id = 1L, nombre = "Test 1", precio = 10.0, cantidad = 1)

        viewModel.disminuirCantidad(producto)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { mockRepo.quitarDelCarrito("test-user", 1L) }
        coVerify(exactly = 0) { mockRepo.disminuirCantidad(any(), any()) }
    }

    @Test
    fun `totalCarrito se calcula correctamente`() = runTest {
        viewModel.setUsuarioId("test-user")
        val carrito = listOf(
            ProductoDto(id = 1L, nombre = "P1", precio = 10.0, cantidad = 2), // 20.0
            ProductoDto(id = 2L, nombre = "P2", precio = 5.0, cantidad = 3)  // 15.0
        )
        coEvery { mockRepo.obtenerCarrito("test-user") } returns carrito

        // Re-load the cart *after* the mock has been set up.
        viewModel.setUsuarioId("test-user")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(35.0, viewModel.totalCarrito.value, 0.0)
    }

    // --- Tests Existentes ---

    @Test
    fun `cargarProductosRemotos con exito actualiza la lista de productos`() = runTest {
        val productos = listOf(ProductoDto(id = 1L, nombre = "Test 1", precio = 10.0))
        coEvery { mockRepo.obtenerProductosRemotos() } returns productos

        viewModel.cargarProductosRemotos()
        testDispatcher.scheduler.advanceUntilIdle()

        val listaProductos = viewModel.productos.first()
        assertEquals(1, listaProductos.size)
        assertEquals("Test 1", listaProductos[0].nombre)
    }

    @Test
    fun `cargarProductosRemotos con error de red`() = runTest {
        coEvery { mockRepo.obtenerProductosRemotos() } throws IOException()

        viewModel.cargarProductosRemotos()
        testDispatcher.scheduler.advanceUntilIdle()

        val productos = viewModel.productos.first()
        assertTrue(productos.isEmpty())
    }

    @Test
    fun `guardarProducto con validacion exitosa guarda el producto`() = runTest {
        viewModel.onNombreCharge("Producto valido")
        viewModel.onDescripcionCharge("desc")
        viewModel.onPrecioCharge("10.0")
        viewModel.onCategoriaCharge("cat")
        viewModel.onImagenCharge(1)

        val resultado = viewModel.guardarProducto()
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(resultado)
        coVerify { mockRepo.guardar(any()) }
        assertTrue(viewModel.estado.value.productoExitoso)
    }

    @Test
    fun `guardarProducto con validacion fallida no guarda`() = runTest {
        viewModel.onNombreCharge("") // Nombre vacío para que falle la validación

        val resultado = viewModel.guardarProducto()
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(resultado)
        coVerify(exactly = 0) { mockRepo.guardar(any()) }
        assertFalse(viewModel.estado.value.productoExitoso)
    }


    @Test
    fun `validarProducto con nombre vacio devuelve error`() {
        viewModel.onNombreCharge("")
        viewModel.onDescripcionCharge("Una descripción válida")
        viewModel.onPrecioCharge("10.0")
        viewModel.onImagenCharge(1)
        viewModel.onCategoriaCharge("Electrónica")

        val esValido = viewModel.validarProducto()

        assertFalse("La validación debería fallar por nombre vacío", esValido)
        val estado = viewModel.estado.value
        assertNotNull(estado.errores.nombre)
        assertEquals("El Campo Es Obligatorio", estado.errores.nombre)
    }

    @Test
    fun `validarProducto con precio invalido devuelve error`() {
        viewModel.onNombreCharge("Producto de prueba")
        viewModel.onDescripcionCharge("Una descripción válida")
        viewModel.onPrecioCharge("0.0")
        viewModel.onImagenCharge(1)
        viewModel.onCategoriaCharge("Ropa")

        val esValido = viewModel.validarProducto()

        assertFalse("La validación debería fallar por precio inválido", esValido)
        val estado = viewModel.estado.value
        assertNotNull(estado.errores.precio)
        assertEquals("El valor debe ser mayor y distinto de cero", estado.errores.precio)
    }

    @Test
    fun `validarProducto con datos correctos devuelve exito`() {
        viewModel.onNombreCharge("Producto Perfecto")
        viewModel.onDescripcionCharge("Funciona como la seda")
        viewModel.onPrecioCharge("99.99")
        viewModel.onImagenCharge(1)
        viewModel.onCategoriaCharge("Hogar")

        val esValido = viewModel.validarProducto()

        assertTrue("La validación debería ser exitosa", esValido)
        val estado = viewModel.estado.value
        assertNull(estado.errores.nombre)
        assertNull(estado.errores.descripcion)
        assertNull(estado.errores.precio)
        assertNull(estado.errores.imagen)
        assertNull(estado.errores.categoria)
    }
}