package com.example.aplicacion

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavController
import com.example.aplicacion.data.remote.dto.ProductoDto
import com.example.aplicacion.ui.screen.HomeScreen
import com.example.aplicacion.viewmodel.ProductoViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun cuandoLaListaDeProductosEsExito_seMuestranLosProductos() {
        // Arrange
        val mockViewModel = mockk<ProductoViewModel>(relaxed = true)
        val mockNavController = mockk<NavController>(relaxed = true)
        val productos = listOf(ProductoDto(id = 1L, nombre = "Producto 1"), ProductoDto(id = 2L, nombre = "Producto 2"))

        every { mockViewModel.productos } returns MutableStateFlow(productos).asStateFlow()
        every { mockViewModel.isLoading } returns MutableStateFlow(false).asStateFlow()
        every { mockViewModel.busquedaCategoria } returns MutableStateFlow("").asStateFlow()
        every { mockViewModel.carrito } returns MutableStateFlow<List<ProductoDto>>(emptyList()).asStateFlow()
        every { mockViewModel.cargarProductosRemotos() } returns Unit 

        // Act
        composeTestRule.setContent {
            com.example.aplicacion.ui.theme.AplicacionTheme {
                HomeScreen(
                    navController = mockNavController,
                    productoViewModel = mockViewModel
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Producto 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Producto 2").assertIsDisplayed()
    }

    @Test
    fun cuandoEstaCargando_seMuestraElIndicadorDeProgreso() {
        // Arrange
        val mockViewModel = mockk<ProductoViewModel>(relaxed = true)
        val mockNavController = mockk<NavController>(relaxed = true)
        
        every { mockViewModel.isLoading } returns MutableStateFlow(true).asStateFlow()
        every { mockViewModel.productos } returns MutableStateFlow<List<ProductoDto>>(emptyList()).asStateFlow()
        every { mockViewModel.busquedaCategoria } returns MutableStateFlow("").asStateFlow()
        every { mockViewModel.carrito } returns MutableStateFlow<List<ProductoDto>>(emptyList()).asStateFlow()

        // Act
        composeTestRule.setContent {
            com.example.aplicacion.ui.theme.AplicacionTheme {
                HomeScreen(
                    navController = mockNavController,
                    productoViewModel = mockViewModel
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithTag("LoadingIndicator").assertIsDisplayed()
    }

    @Test
    fun cuandoLaListaEstaVacia_seMuestraElMensajeDeListaVacia() {
        // Arrange
        val mockViewModel = mockk<ProductoViewModel>(relaxed = true)
        val mockNavController = mockk<NavController>(relaxed = true)

        every { mockViewModel.productos } returns MutableStateFlow<List<ProductoDto>>(emptyList()).asStateFlow()
        every { mockViewModel.isLoading } returns MutableStateFlow(false).asStateFlow()
        every { mockViewModel.busquedaCategoria } returns MutableStateFlow("").asStateFlow()
        every { mockViewModel.carrito } returns MutableStateFlow<List<ProductoDto>>(emptyList()).asStateFlow()
        every { mockViewModel.cargarProductosRemotos() } returns Unit

        // Act
        composeTestRule.setContent {
            com.example.aplicacion.ui.theme.AplicacionTheme {
                HomeScreen(
                    navController = mockNavController,
                    productoViewModel = mockViewModel
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("No hay productos disponibles").assertIsDisplayed()
    }
}