package com.example.aplicacion

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.aplicacion.data.remote.dto.ProductoDto
import com.example.aplicacion.ui.components.ProductCard
import com.example.aplicacion.viewmodel.ProductoViewModel
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class ProductCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun cuandoElProductoNoEstaEnCarrito_elBotonAgrega() {
        // Arrange
        val mockViewModel = mockk<ProductoViewModel>(relaxed = true)
        val producto = ProductoDto(id = 1L, nombre = "Test Product", precio = 99.99)

        composeTestRule.setContent {
            com.example.aplicacion.ui.theme.AplicacionTheme {
                ProductCard(
                    producto = producto,
                    vm = mockViewModel,
                    estaEnCarrito = false
                )
            }
        }

        // Assert: Check initial state
        composeTestRule.onNodeWithText("Agregar al carrito").assertIsDisplayed()

        // Act: Click the button
        composeTestRule.onNodeWithText("Agregar al carrito").performClick()

        // Assert: Verify ViewModel interaction
        verify { mockViewModel.agregarAlCarrito(producto) }
    }

    @Test
    fun cuandoElProductoEstaEnCarrito_elBotonQuita() {
        // Arrange
        val mockViewModel = mockk<ProductoViewModel>(relaxed = true)
        val producto = ProductoDto(id = 1L, nombre = "Test Product", precio = 99.99)

        composeTestRule.setContent {
            com.example.aplicacion.ui.theme.AplicacionTheme {
                ProductCard(
                    producto = producto,
                    vm = mockViewModel,
                    estaEnCarrito = true
                )
            }
        }

        // Assert: Check initial state
        composeTestRule.onNodeWithText("Quitar del carrito").assertIsDisplayed()

        // Act: Click the button
        composeTestRule.onNodeWithText("Quitar del carrito").performClick()

        // Assert: Verify ViewModel interaction
        verify { mockViewModel.quitarDelCarrito(producto) }
    }
}