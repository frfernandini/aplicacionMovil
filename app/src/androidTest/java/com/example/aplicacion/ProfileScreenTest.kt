package com.example.aplicacion

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.example.aplicacion.ui.screen.ProfileScreen
import com.example.aplicacion.viewmodel.PerfilViewModel
import com.example.aplicacion.model.PerfilUiState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class ProfileScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun alCargarLaPantalla_seMuestranLosDatosDelUsuarioYLasOpciones() {
        // Arrange
        val mockViewModel = mockk<PerfilViewModel>(relaxed = true)
        val mockNavController = mockk<NavController>(relaxed = true)
        val uiState = PerfilUiState(nombreUsuario = "Test User", email = "test@example.com")

        every { mockViewModel.perfilState } returns MutableStateFlow(uiState)

        // Act
        composeTestRule.setContent {
            com.example.aplicacion.ui.theme.AplicacionTheme {
                ProfileScreen(navController = mockNavController, profileViewModel = mockViewModel)
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Test User").assertIsDisplayed()
        composeTestRule.onNodeWithText("test@example.com").assertIsDisplayed()
        composeTestRule.onNodeWithText("Editar Perfil").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cerrar Sesión").assertIsDisplayed()
    }

    @Test
    fun alHacerClicEnEditarPerfil_seNavegaALaPantallaDeEdicion() {
        // Arrange
        val mockViewModel = mockk<PerfilViewModel>(relaxed = true)
        val mockNavController = mockk<NavController>(relaxed = true)
        every { mockViewModel.perfilState } returns MutableStateFlow(PerfilUiState())

        composeTestRule.setContent {
            com.example.aplicacion.ui.theme.AplicacionTheme {
                ProfileScreen(navController = mockNavController, profileViewModel = mockViewModel)
            }
        }

        // Act
        composeTestRule.onNodeWithText("Editar Perfil").performClick()

        // Assert
        verify { mockNavController.navigate("editar_perfil") }
    }

    @Test
    fun alHacerClicEnCerrarSesion_seLlamaAlViewModel() {
        // Arrange
        val mockViewModel = mockk<PerfilViewModel>(relaxed = true)
        val mockNavController = mockk<NavController>(relaxed = true)
        every { mockViewModel.perfilState } returns MutableStateFlow(PerfilUiState())

        composeTestRule.setContent {
            com.example.aplicacion.ui.theme.AplicacionTheme {
                ProfileScreen(navController = mockNavController, profileViewModel = mockViewModel)
            }
        }

        // Act
        composeTestRule.onNodeWithText("Cerrar Sesión").performClick()

        // Assert
        verify { mockViewModel.onCerrarSesion() }
    }

    @Test
    fun cuandoElLogoutEsExitoso_seNavegaALaPantallaDeLogin() {
        // Arrange
        val mockViewModel = mockk<PerfilViewModel>(relaxed = true)
        val mockNavController = mockk<NavController>(relaxed = true)
        val stateFlow = MutableStateFlow(PerfilUiState(logoutExitoso = false))
        every { mockViewModel.perfilState } returns stateFlow

        composeTestRule.setContent {
            com.example.aplicacion.ui.theme.AplicacionTheme {
                ProfileScreen(navController = mockNavController, profileViewModel = mockViewModel)
            }
        }

        // Act
        composeTestRule.runOnIdle {
            stateFlow.value = stateFlow.value.copy(logoutExitoso = true)
        }
        composeTestRule.waitForIdle()

        // Assert
        verify { mockNavController.navigate(eq("login"), any<NavOptionsBuilder.() -> Unit>()) }
        verify { mockViewModel.onNavegacionRealizada() }
    }
}