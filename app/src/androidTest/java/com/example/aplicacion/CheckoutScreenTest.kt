package com.example.aplicacion

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavController
import com.example.aplicacion.data.remote.dto.ProductoDto
import com.example.aplicacion.ui.screen.CheckoutScreen
import com.example.aplicacion.viewmodel.CheckoutViewModel
import com.example.aplicacion.viewmodel.CheckoutUiState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class CheckoutScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun alMostrarLaPantalla_seMuestraElResumenYElTotal() {
        // Arrange
        val mockViewModel = mockk<CheckoutViewModel>(relaxed = true)
        val mockNavController = mockk<NavController>(relaxed = true)
        val carrito = listOf(ProductoDto(id = 1L, nombre = "Producto 1", precio = 10.0, cantidad = 2))
        val total = 20.0

        every { mockViewModel.uiState } returns MutableStateFlow(CheckoutUiState())

        // Act
        composeTestRule.setContent {
            com.example.aplicacion.ui.theme.AplicacionTheme {
                CheckoutScreen(
                    navController = mockNavController,
                    checkoutViewModel = mockViewModel,
                    carrito = carrito,
                    total = total,
                    onPedidoExitoso = {}
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("2x Producto 1").assertIsDisplayed()
        composeTestRule.onAllNodesWithText("$20.00").assertCountEquals(2)
        composeTestRule.onNodeWithText("Confirmar Pedido").assertIsDisplayed()
    }

    @Test
    fun alConfirmarConDireccionVacia_seMuestraError() {
        // Arrange
        val mockViewModel = mockk<CheckoutViewModel>(relaxed = true)
        val mockNavController = mockk<NavController>(relaxed = true)
        val errorState = CheckoutUiState(errorDireccion = "La dirección es obligatoria")

        every { mockViewModel.uiState } returns MutableStateFlow(errorState)

        // Act
        composeTestRule.setContent {
            com.example.aplicacion.ui.theme.AplicacionTheme {
                CheckoutScreen(
                    navController = mockNavController,
                    checkoutViewModel = mockViewModel,
                    carrito = emptyList(),
                    total = 0.0,
                    onPedidoExitoso = {}
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("La dirección es obligatoria").assertIsDisplayed()
    }

    @Test
    fun alHacerClicEnConfirmarPedido_seLlamaAlViewModel() {
        // Arrange
        val mockViewModel = mockk<CheckoutViewModel>(relaxed = true)
        val mockNavController = mockk<NavController>(relaxed = true)
        val carrito = listOf(ProductoDto(id = 1L, nombre = "Producto 1"))

        every { mockViewModel.uiState } returns MutableStateFlow(CheckoutUiState(direccion = "Calle Falsa 123"))

        composeTestRule.setContent {
            com.example.aplicacion.ui.theme.AplicacionTheme {
                CheckoutScreen(
                    navController = mockNavController,
                    checkoutViewModel = mockViewModel,
                    carrito = carrito,
                    total = 10.0,
                    onPedidoExitoso = {}
                )
            }
        }

        // Act
        composeTestRule.onNodeWithText("Confirmar Pedido").performClick()

        // Assert
        verify { mockViewModel.crearPedido(carrito) }
    }

    @Test
    fun cuandoEstaCargando_elBotonSeDeshabilitaYMustraElLoader() {
        // Arrange
        val mockViewModel = mockk<CheckoutViewModel>(relaxed = true)
        val mockNavController = mockk<NavController>(relaxed = true)

        every { mockViewModel.uiState } returns MutableStateFlow(CheckoutUiState(isLoading = true))

        // Act
        composeTestRule.setContent {
            com.example.aplicacion.ui.theme.AplicacionTheme {
                CheckoutScreen(
                    navController = mockNavController,
                    checkoutViewModel = mockViewModel,
                    carrito = emptyList(),
                    total = 0.0,
                    onPedidoExitoso = {}
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Confirmar Pedido").assertDoesNotExist()
        composeTestRule.onNodeWithTag("LoadingButtonIndicator").assertIsDisplayed()
        composeTestRule.onNodeWithTag("LoadingButtonIndicator").onParent().assertIsNotEnabled()
    }

    @Test
    fun cuandoElPedidoEsExitoso_seLlamaALaNavegacion() {
        // Arrange
        val mockViewModel = mockk<CheckoutViewModel>(relaxed = true)
        val mockNavController = mockk<NavController>(relaxed = true)
        val onPedidoExitosoMock = mockk<() -> Unit>(relaxed = true)
        val stateFlow = MutableStateFlow(CheckoutUiState(pedidoExitoso = false))

        every { mockViewModel.uiState } returns stateFlow

        composeTestRule.setContent {
            com.example.aplicacion.ui.theme.AplicacionTheme {
                CheckoutScreen(
                    navController = mockNavController,
                    checkoutViewModel = mockViewModel,
                    carrito = emptyList(),
                    total = 0.0,
                    onPedidoExitoso = onPedidoExitosoMock
                )
            }
        }

        // Act
        composeTestRule.runOnIdle {
            stateFlow.value = stateFlow.value.copy(pedidoExitoso = true)
        }
        composeTestRule.waitForIdle()

        // Assert
        verify { onPedidoExitosoMock() }
        verify { mockViewModel.onNavegacionRealizada() }
    }
}