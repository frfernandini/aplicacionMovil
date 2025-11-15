package com.example.aplicacion

import android.app.Application
import com.example.aplicacion.data.SessionManager
import com.example.aplicacion.data.remote.dto.AuthResponse
import com.example.aplicacion.data.remote.dto.LoginRequest
import com.example.aplicacion.model.repository.UsuarioRepository
import com.example.aplicacion.viewmodel.LoginViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val mockRepo = mockk<UsuarioRepository>(relaxed = true) // relaxed = true para no mockear todos los métodos
    private val mockApplication = mockk<Application>(relaxed = true)
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(mockApplication, mockRepo)
        // Limpiar el SessionManager antes de cada test
        SessionManager.authToken = ""
        SessionManager.userId = ""
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `iniciarSesion con formulario invalido no llama al repositorio`() = runTest {
        viewModel.onCorreoChange("")
        viewModel.onClaveChange("")

        viewModel.iniciarSesion()
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 0) { mockRepo.loginUsuario(any()) }
    }


    @Test
    fun `iniciarSesion con exito guarda los datos de sesion`() = runTest {
        val loginRequest = LoginRequest("user@test.com", "password123")
        val authResponse = AuthResponse(token = "fake-token", id = "user-1", nombre = "Test User")
        coEvery { mockRepo.loginUsuario(loginRequest) } returns authResponse

        viewModel.onCorreoChange("user@test.com")
        viewModel.onClaveChange("password123")
        viewModel.iniciarSesion()
        testDispatcher.scheduler.advanceUntilIdle()

        // Verificar que los datos se guardaron en SessionManager
        assertEquals("fake-token", SessionManager.authToken)
        assertEquals("user-1", SessionManager.userId)
    }

    @Test
    fun `onNavegacionRealizada resetea el estado de loginExitoso`() = runTest {
        // Arrange: successful login
        coEvery { mockRepo.loginUsuario(any()) } returns AuthResponse(token = "fake-token", id = "user-1", nombre = "Test User")
        viewModel.onCorreoChange("user@test.com")
        viewModel.onClaveChange("password123")
        viewModel.iniciarSesion()
        testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(viewModel.loginState.value.loginExitoso)

        // Act
        viewModel.onNavegacionRealizada()

        // Assert
        assertFalse(viewModel.loginState.value.loginExitoso)
    }

    @Test
    fun `iniciarSesion con campos vacios devuelve error`() {
        viewModel.onCorreoChange("")
        viewModel.onClaveChange("")

        viewModel.iniciarSesion()

        val estado = viewModel.loginState.value
        assertFalse(estado.loginExitoso)
        assertNotNull(estado.error)
        assertEquals("Correo o contraseña no pueden estar vacíos", estado.error)
    }

    @Test
    fun `iniciarSesion con credenciales invalidas devuelve error`() = runTest {
        val loginRequest = LoginRequest("user@test.com", "wrongpass")
        coEvery { mockRepo.loginUsuario(loginRequest) } returns null

        viewModel.onCorreoChange("user@test.com")
        viewModel.onClaveChange("wrongpass")

        viewModel.iniciarSesion()
        testDispatcher.scheduler.advanceUntilIdle()

        val estado = viewModel.loginState.value
        assertFalse(estado.loginExitoso)
        assertNotNull(estado.error)
        assertEquals("Credenciales inválidas. Por favor, inténtalo de nuevo.", estado.error)
    }

    @Test
    fun `iniciarSesion con credenciales validas devuelve exito`() = runTest {
        val loginRequest = LoginRequest("user@test.com", "password123")
        val authResponse = AuthResponse(token = "fake-token", id = "user-1", nombre = "Test User")
        coEvery { mockRepo.loginUsuario(loginRequest) } returns authResponse

        viewModel.onCorreoChange("user@test.com")
        viewModel.onClaveChange("password123")

        viewModel.iniciarSesion()
        testDispatcher.scheduler.advanceUntilIdle()

        val estado = viewModel.loginState.value
        assertTrue(estado.loginExitoso)
        assertNull(estado.error)
        assertEquals("user-1", estado.userId)
    }

    @Test
    fun `iniciarSesion con error de conexion devuelve error`() = runTest {
        val loginRequest = LoginRequest("user@test.com", "password123")
        val errorMessage = "No se pudo conectar al servidor"
        coEvery { mockRepo.loginUsuario(loginRequest) } throws IOException(errorMessage)

        viewModel.onCorreoChange("user@test.com")
        viewModel.onClaveChange("password123")

        viewModel.iniciarSesion()
        testDispatcher.scheduler.advanceUntilIdle()

        val estado = viewModel.loginState.value
        assertFalse(estado.loginExitoso)
        assertNotNull(estado.error)
        assertTrue(estado.error!!.contains("Error de conexión"))
    }
}