package com.example.aplicacion

import com.example.aplicacion.data.remote.dto.AuthResponse
import com.example.aplicacion.data.remote.dto.RegistroRequest
import com.example.aplicacion.model.repository.UsuarioRepository
import com.example.aplicacion.viewmodel.RegistroViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class RegistroViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val mockRepo = mockk<UsuarioRepository>(relaxed = true)
    private lateinit var viewModel: RegistroViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RegistroViewModel(mockRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `validarFormulario sin aceptar terminos falla la validacion`() {
        // Arrange
        viewModel.onNombreChange("Test User")
        viewModel.onCorreoChange("test@example.com")
        viewModel.onClaveChange("password123")
        viewModel.onDireccionChange("123 Test St")
        viewModel.onAceptarTerminosChange(false) // No se aceptan los términos

        // Act
        val esValido = viewModel.validarFormulario()

        // Assert
        assertFalse("La validación debería fallar si no se aceptan los términos", esValido)
        val estado = viewModel.estado.value
        assertNotNull(estado.errores.aceptaTerminos)
        assertEquals("Debes aceptar los términos", estado.errores.aceptaTerminos)
    }


    @Test
    fun `registrarUsuario con formulario invalido no llama al repositorio`() = runTest {
        viewModel.onNombreChange("") // Nombre vacío para invalidar el formulario
        viewModel.onAceptarTerminosChange(true)

        viewModel.registrarUsuario()
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 0) { mockRepo.registrarUsuarioRemoto(any()) }
    }

    @Test
    fun `onNavegacionRealizada resetea el estado de registroExitoso`() = runTest {
        // Arrange: successful registration
        coEvery { mockRepo.registrarUsuarioRemoto(any()) } returns Response.success(AuthResponse("fake-token", "user-1", "Test User"))
        viewModel.onNombreChange("Test User")
        viewModel.onCorreoChange("test@example.com")
        viewModel.onClaveChange("password123")
        viewModel.onDireccionChange("123 Test St")
        viewModel.onAceptarTerminosChange(true)

        viewModel.registrarUsuario()
        testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(viewModel.estado.value.registroExitoso)

        // Act
        viewModel.onNavegacionRealizada()

        // Assert
        assertFalse(viewModel.estado.value.registroExitoso)
    }

    @Test
    fun `validarFormulario con nombre vacio falla la validacion`() {
        viewModel.onNombreChange("")
        viewModel.onCorreoChange("test@test.com")
        viewModel.onClaveChange("123456")
        viewModel.onDireccionChange("Una calle 123")
        viewModel.onAceptarTerminosChange(true)

        val esValido = viewModel.validarFormulario()

        assertFalse("La validación debería fallar con un nombre vacío.", esValido)
        val estado = viewModel.estado.value
        assertEquals("campo obligatorio", estado.errores.nombre)
    }

    @Test
    fun `registrarUsuario con exito actualiza el estado a exitoso`() = runTest {
        val request = RegistroRequest("Test User", "test@example.com", "password123", "123 Test St")
        val response = Response.success(AuthResponse("fake-token", "user-1", "Test User"))
        coEvery { mockRepo.registrarUsuarioRemoto(any()) } returns response

        viewModel.onNombreChange(request.nombre)
        viewModel.onCorreoChange(request.email)
        viewModel.onClaveChange(request.password)
        viewModel.onDireccionChange(request.direccion)
        viewModel.onAceptarTerminosChange(true)

        viewModel.registrarUsuario()
        testDispatcher.scheduler.advanceUntilIdle()

        val estado = viewModel.estado.value
        assertTrue(estado.registroExitoso)
    }

    @Test
    fun `registrarUsuario con correo existente devuelve error`() = runTest {
        val request = RegistroRequest("Test User", "test@example.com", "password123", "123 Test St")
        val errorResponse = Response.error<AuthResponse>(400, "".toResponseBody(null))
        coEvery { mockRepo.registrarUsuarioRemoto(any()) } returns errorResponse

        viewModel.onNombreChange(request.nombre)
        viewModel.onCorreoChange(request.email)
        viewModel.onClaveChange(request.password)
        viewModel.onDireccionChange(request.direccion)
        viewModel.onAceptarTerminosChange(true)

        viewModel.registrarUsuario()
        testDispatcher.scheduler.advanceUntilIdle()

        val estado = viewModel.estado.value
        assertFalse(estado.registroExitoso)
        assertEquals("El correo ya esta registrado o hubo un error.", estado.errores.correo)
    }

    @Test
    fun `registrarUsuario con error de red devuelve error`() = runTest {
        coEvery { mockRepo.registrarUsuarioRemoto(any()) } throws IOException("Network error")

        viewModel.onNombreChange("Test User")
        viewModel.onCorreoChange("test@example.com")
        viewModel.onClaveChange("password123")
        viewModel.onDireccionChange("123 Test St")
        viewModel.onAceptarTerminosChange(true)

        viewModel.registrarUsuario()
        testDispatcher.scheduler.advanceUntilIdle()

        val estado = viewModel.estado.value
        assertFalse(estado.registroExitoso)
        assertEquals("No se pudo conectar al servidor. Intenta de nuevo.", estado.errores.nombre)
    }

    @Test
    fun `validarFormulario con correo invalido falla la validacion`() {
        viewModel.onNombreChange("Usuario de Prueba")
        viewModel.onCorreoChange("correo-invalido")
        viewModel.onClaveChange("123456")
        viewModel.onDireccionChange("Una calle 123")
        viewModel.onAceptarTerminosChange(true)

        val esValido = viewModel.validarFormulario()

        assertFalse(esValido)
        assertEquals("Correo Invalido", viewModel.estado.value.errores.correo)
    }

    @Test
    fun `validarFormulario con clave corta falla la validacion`() {
        viewModel.onNombreChange("Usuario de Prueba")
        viewModel.onCorreoChange("test@test.com")
        viewModel.onClaveChange("123")
        viewModel.onDireccionChange("Una calle 123")
        viewModel.onAceptarTerminosChange(true)

        val esValido = viewModel.validarFormulario()

        assertFalse(esValido)
        assertEquals("Debe tener al menos 6 caracteres", viewModel.estado.value.errores.clave)
    }

    @Test
    fun `validarFormulario con datos validos retorna exito`() {
        viewModel.onNombreChange("Usuario de Prueba")
        viewModel.onCorreoChange("test@test.com")
        viewModel.onClaveChange("123456")
        viewModel.onDireccionChange("Una Calle Valida 123")
        viewModel.onAceptarTerminosChange(true)

        val esValido = viewModel.validarFormulario()

        assertTrue("La validación debería ser exitosa.", esValido)
        val errores = viewModel.estado.value.errores
        assertNull(errores.nombre)
        assertNull(errores.correo)
        assertNull(errores.clave)
        assertNull(errores.direccion)
        assertNull(errores.aceptaTerminos)
    }
}