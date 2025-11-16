package com.example.aplicacion

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.example.aplicacion.model.UsuarioErrores
import com.example.aplicacion.model.UsuarioUiState
import com.example.aplicacion.ui.screen.RegistroScreen
import com.example.aplicacion.viewmodel.RegistroViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Rule
import org.junit.Test

class RegistroScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun registroScreen_seMuestranTodosLosComponentes() {
        
        val mockViewModel = mockk<RegistroViewModel>(relaxed = true)
        val mockNavController = mockk<NavController>(relaxed = true)
        every { mockViewModel.estado } returns MutableStateFlow(UsuarioUiState()).asStateFlow()


        composeTestRule.setContent {
            com.example.aplicacion.ui.theme.AplicacionTheme {
                RegistroScreen(
                    navController = mockNavController,
                    viewModel = mockViewModel
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Crear cuenta").assertIsDisplayed()
        composeTestRule.onNodeWithText("Nombre").assertIsDisplayed()
        composeTestRule.onNodeWithText("Correo electrónico").assertIsDisplayed()
        composeTestRule.onNodeWithText("Contraseña").assertIsDisplayed()
        composeTestRule.onNodeWithText("Dirección").assertIsDisplayed()
        composeTestRule.onNodeWithText("Acepto los términos y condiciones").assertIsDisplayed()
        composeTestRule.onNodeWithText("Registrar").assertIsDisplayed()
    }

    @Test
    fun alHacerClicEnRegistrar_conDatosValidos_seLlamaAlViewModel() {

        val mockViewModel = mockk<RegistroViewModel>(relaxed = true)
        val mockNavController = mockk<NavController>(relaxed = true)
        every { mockViewModel.estado } returns MutableStateFlow(UsuarioUiState()).asStateFlow()

        composeTestRule.setContent {
            com.example.aplicacion.ui.theme.AplicacionTheme {
                RegistroScreen(
                    navController = mockNavController,
                    viewModel = mockViewModel
                )
            }
        }


        composeTestRule.onNodeWithText("Nombre").performTextInput("Test User")
        composeTestRule.onNodeWithText("Correo electrónico").performTextInput("test@test.com")
        composeTestRule.onNodeWithText("Contraseña").performTextInput("password123")
        composeTestRule.onNodeWithText("Dirección").performTextInput("123 Test St")
        composeTestRule.onNodeWithText("Acepto los términos y condiciones").performClick()
        composeTestRule.onNodeWithText("Registrar").performClick()

        // Assert: Verify that the business logic was called
        verify { mockViewModel.registrarUsuario() }
    }

    @Test
    fun cuandoHayUnError_seMuestraElMensajeDeError() {
        // Arrange
        val mockViewModel = mockk<RegistroViewModel>(relaxed = true)
        val mockNavController = mockk<NavController>(relaxed = true)
        val errorState = UsuarioUiState(errores = UsuarioErrores(nombre = "El nombre es obligatorio"))
        val stateFlow = MutableStateFlow(errorState)
        every { mockViewModel.estado } returns stateFlow

        // Act
        composeTestRule.setContent {
            com.example.aplicacion.ui.theme.AplicacionTheme {
                RegistroScreen(
                    navController = mockNavController,
                    viewModel = mockViewModel
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("El nombre es obligatorio").assertIsDisplayed()
    }

    @Test
    fun cuandoElRegistroEsExitoso_seNavegaALaPantallaDeLogin() {
        // Arrange
        val mockViewModel = mockk<RegistroViewModel>(relaxed = true)
        val mockNavController = mockk<NavController>(relaxed = true)
        val stateFlow = MutableStateFlow(UsuarioUiState(registroExitoso = false))
        every { mockViewModel.estado } returns stateFlow

        composeTestRule.setContent {
            com.example.aplicacion.ui.theme.AplicacionTheme {
                RegistroScreen(
                    navController = mockNavController,
                    viewModel = mockViewModel
                )
            }
        }


        composeTestRule.runOnIdle {
            stateFlow.value = stateFlow.value.copy(registroExitoso = true)
        }


        composeTestRule.waitForIdle()

        // Assert: Verify that navigation was triggered and the state was reset
        verify { mockNavController.navigate(eq("login"), any<NavOptionsBuilder.() -> Unit>()) }
        verify { mockViewModel.onNavegacionRealizada() }
    }
}