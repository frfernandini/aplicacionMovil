package com.example.aplicacion

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavController
import com.example.aplicacion.model.UsuarioUiState
import com.example.aplicacion.ui.screen.RegistroScreen
import com.example.aplicacion.viewmodel.RegistroViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Rule
import org.junit.Test

class RegistroScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun registroScreen_seMuestranTodosLosComponentes() {
        // Arrange
        val mockViewModel = mockk<RegistroViewModel>(relaxed = true)
        val mockNavController = mockk<NavController>(relaxed = true)

        // Define the expected state for the UI
        val initialUiState = UsuarioUiState()
        val stateFlow = MutableStateFlow(initialUiState)

        // Mock the ViewModel to return the specific state
        every { mockViewModel.estado } returns stateFlow.asStateFlow()

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
        composeTestRule.onNodeWithText("Crear cuenta").assertIsDisplayed()
        composeTestRule.onNodeWithText("Nombre").assertIsDisplayed()
        composeTestRule.onNodeWithText("Correo electrónico").assertIsDisplayed()
        composeTestRule.onNodeWithText("Contraseña").assertIsDisplayed()
        composeTestRule.onNodeWithText("Dirección").assertIsDisplayed()
        composeTestRule.onNodeWithText("Acepto los términos y condiciones").assertIsDisplayed()
        composeTestRule.onNodeWithText("Registrar").assertIsDisplayed()
    }
}