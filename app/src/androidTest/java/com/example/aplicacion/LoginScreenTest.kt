package com.example.aplicacion

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.example.aplicacion.model.LoginUiState
import com.example.aplicacion.ui.screen.LoginScreen
import com.example.aplicacion.viewmodel.LoginViewModel
import com.example.aplicacion.viewmodel.ProductoViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_seMuestranTodosLosComponentes() {
        // Arrange
        val mockLoginViewModel = mockk<LoginViewModel>(relaxed = true)
        val mockProductoViewModel = mockk<ProductoViewModel>(relaxed = true)
        val mockNavController = mockk<NavController>(relaxed = true)
        every { mockLoginViewModel.loginState } returns MutableStateFlow(LoginUiState()).asStateFlow()

        // Act
        composeTestRule.setContent {
            com.example.aplicacion.ui.theme.AplicacionTheme {
                LoginScreen(
                    navController = mockNavController,
                    loginViewModel = mockLoginViewModel,
                    productoViewModel = mockProductoViewModel
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Iniciar Sesión").assertIsDisplayed()
        composeTestRule.onNodeWithText("Correo electrónico").assertIsDisplayed()
        composeTestRule.onNodeWithText("Contraseña").assertIsDisplayed()
        composeTestRule.onNodeWithText("Entrar").assertIsDisplayed() // Corregido
    }

    @Test
    fun alHacerClicEnIniciarSesion_conDatosValidos_seLlamaAlViewModel() {
        // Arrange
        val mockLoginViewModel = mockk<LoginViewModel>(relaxed = true)
        val mockProductoViewModel = mockk<ProductoViewModel>(relaxed = true)
        val mockNavController = mockk<NavController>(relaxed = true)
        every { mockLoginViewModel.loginState } returns MutableStateFlow(LoginUiState()).asStateFlow()

        composeTestRule.setContent {
            com.example.aplicacion.ui.theme.AplicacionTheme {
                LoginScreen(
                    navController = mockNavController,
                    loginViewModel = mockLoginViewModel,
                    productoViewModel = mockProductoViewModel
                )
            }
        }

        // Act
        composeTestRule.onNodeWithText("Correo electrónico").performTextInput("test@test.com")
        composeTestRule.onNodeWithText("Contraseña").performTextInput("password123")
        composeTestRule.onNodeWithText("Entrar").performClick() // Corregido

        // Assert
        verify { mockLoginViewModel.iniciarSesion() }
    }

    @Test
    fun cuandoHayUnError_seMuestraElMensajeDeError() {
        // Arrange
        val mockLoginViewModel = mockk<LoginViewModel>(relaxed = true)
        val mockProductoViewModel = mockk<ProductoViewModel>(relaxed = true)
        val mockNavController = mockk<NavController>(relaxed = true)
        val errorState = LoginUiState(error = "Credenciales inválidas")
        val stateFlow = MutableStateFlow(errorState)
        every { mockLoginViewModel.loginState } returns stateFlow

        // Act
        composeTestRule.setContent {
            com.example.aplicacion.ui.theme.AplicacionTheme {
                LoginScreen(
                    navController = mockNavController,
                    loginViewModel = mockLoginViewModel,
                    productoViewModel = mockProductoViewModel
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Credenciales inválidas").assertIsDisplayed()
    }

    @Test
    fun cuandoElLoginEsExitoso_seNavegaALaPantallaPrincipal() {
        // Arrange
        val mockLoginViewModel = mockk<LoginViewModel>(relaxed = true)
        val mockProductoViewModel = mockk<ProductoViewModel>(relaxed = true)
        val mockNavController = mockk<NavController>(relaxed = true)
        val testUserId = "user-123"
        val stateFlow = MutableStateFlow(LoginUiState(loginExitoso = false, userId = null))
        every { mockLoginViewModel.loginState } returns stateFlow

        composeTestRule.setContent {
            com.example.aplicacion.ui.theme.AplicacionTheme {
                LoginScreen(
                    navController = mockNavController,
                    loginViewModel = mockLoginViewModel,
                    productoViewModel = mockProductoViewModel
                )
            }
        }

        // Act: Simulate a successful login by updating the state
        composeTestRule.runOnIdle {
            stateFlow.value = stateFlow.value.copy(loginExitoso = true, userId = testUserId)
        }
        composeTestRule.waitForIdle() // Wait for the UI to react to the state change

        // Assert: Verify navigation to the correct route and that the state was reset
        verify { mockNavController.navigate(eq("home"), any<NavOptionsBuilder.() -> Unit>()) }
        verify { mockLoginViewModel.onNavegacionRealizada() }
        verify { mockProductoViewModel.setUsuarioId(testUserId) }
    }
}