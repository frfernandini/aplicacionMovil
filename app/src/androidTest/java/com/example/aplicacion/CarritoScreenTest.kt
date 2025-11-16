package com.example.aplicacion

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.aplicacion.data.remote.dto.ProductoDto
import com.example.aplicacion.ui.screen.CarritoScreen
import com.example.aplicacion.viewmodel.ProductoViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Rule
import org.junit.Test

class CarritoScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun cuandoElCarritoEstaVacio_seMuestraElMensajeCorrecto() {
        // Arrange
        val mockViewModel = mockk<ProductoViewModel>(relaxed = true)

        every { mockViewModel.carrito } returns MutableStateFlow<List<ProductoDto>>(emptyList()).asStateFlow()
        every { mockViewModel.totalCarrito } returns MutableStateFlow(0.0).asStateFlow()
        every { mockViewModel.isLoading } returns MutableStateFlow(false).asStateFlow()

        // Act
        composeTestRule.setContent {
            com.example.aplicacion.ui.theme.AplicacionTheme {
                CarritoScreen(
                    vm = mockViewModel,
                    onBack = {},
                    onPagar = {}
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("CARRITO VACIO").assertIsDisplayed()
    }

    @Test
    fun cuandoHayProductosEnElCarrito_seMuestranLosItemsYElTotal() {
        // Arrange
        val mockViewModel = mockk<ProductoViewModel>(relaxed = true)
        val productos = listOf(ProductoDto(id = 1L, nombre = "Producto 1", precio = 10.0, cantidad = 2))

        every { mockViewModel.carrito } returns MutableStateFlow(productos).asStateFlow()
        every { mockViewModel.totalCarrito } returns MutableStateFlow(20.0).asStateFlow()
        every { mockViewModel.isLoading } returns MutableStateFlow(false).asStateFlow()

        // Act
        composeTestRule.setContent {
            com.example.aplicacion.ui.theme.AplicacionTheme {
                CarritoScreen(
                    vm = mockViewModel,
                    onBack = {},
                    onPagar = {}
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Producto 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Total: $20.00").assertIsDisplayed()
    }

    @Test
    fun alHacerClicEnAumentar_seLlamaAlViewModel() {
        // Arrange
        val mockViewModel = mockk<ProductoViewModel>(relaxed = true)
        val producto = ProductoDto(id = 1L, nombre = "Producto 1", precio = 10.0, cantidad = 1)
        every { mockViewModel.carrito } returns MutableStateFlow(listOf(producto)).asStateFlow()
        every { mockViewModel.isLoading } returns MutableStateFlow(false).asStateFlow()
        every { mockViewModel.totalCarrito } returns MutableStateFlow(10.0).asStateFlow()

        composeTestRule.setContent {
            com.example.aplicacion.ui.theme.AplicacionTheme {
                CarritoScreen(
                    vm = mockViewModel,
                    onBack = {},
                    onPagar = {}
                )
            }
        }

        // Act
        composeTestRule.onNodeWithContentDescription("Aumentar cantidad").performClick()

        // Assert
        verify { mockViewModel.aumentarCantidad(producto) }
    }

    @Test
    fun alHacerClicEnDisminuir_seLlamaAlViewModel() {
        // Arrange
        val mockViewModel = mockk<ProductoViewModel>(relaxed = true)
        val producto = ProductoDto(id = 1L, nombre = "Producto 1", precio = 10.0, cantidad = 2)
        every { mockViewModel.carrito } returns MutableStateFlow(listOf(producto)).asStateFlow()
        every { mockViewModel.isLoading } returns MutableStateFlow(false).asStateFlow()
        every { mockViewModel.totalCarrito } returns MutableStateFlow(20.0).asStateFlow()

        composeTestRule.setContent {
            com.example.aplicacion.ui.theme.AplicacionTheme {
                CarritoScreen(
                    vm = mockViewModel,
                    onBack = {},
                    onPagar = {}
                )
            }
        }

        // Act
        composeTestRule.onNodeWithContentDescription("Disminuir cantidad").performClick()

        // Assert
        verify { mockViewModel.disminuirCantidad(producto) }
    }

    @Test
    fun alHacerClicEnVaciarCarrito_seLlamaAlViewModel() {
        // Arrange
        val mockViewModel = mockk<ProductoViewModel>(relaxed = true)
        val productos = listOf(ProductoDto(id = 1L, nombre = "Producto 1"))
        every { mockViewModel.carrito } returns MutableStateFlow(productos).asStateFlow()
        every { mockViewModel.isLoading } returns MutableStateFlow(false).asStateFlow()
        every { mockViewModel.totalCarrito } returns MutableStateFlow(10.0).asStateFlow()

        composeTestRule.setContent {
            com.example.aplicacion.ui.theme.AplicacionTheme {
                CarritoScreen(
                    vm = mockViewModel,
                    onBack = {},
                    onPagar = {}
                )
            }
        }

        // Act
        composeTestRule.onNodeWithContentDescription("Vaciar carrito").performClick()

        // Assert
        verify { mockViewModel.vaciarCarrito() }
    }
}