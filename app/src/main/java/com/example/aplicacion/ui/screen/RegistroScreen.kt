package com.example.aplicacion.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplicacion.R
import com.example.aplicacion.ui.components.FormularioTextField // <-- PASO 1: IMPORTAR TU COMPONENTE
import com.example.aplicacion.viewmodel.UsuarioViewModel
import androidx.compose.runtime.LaunchedEffect
@Composable
fun RegistroScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
) {
    val estado by viewModel.estado.collectAsState()
    LaunchedEffect(key1 = estado.registroExitoso) {
        if (estado.registroExitoso) {
            navController.navigate("login") {
                popUpTo("registro") { inclusive = true }
            }
            viewModel.onNavegacionRealizada()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Usar el color de fondo general
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo_level_up),
            contentDescription = "Logo",
            modifier = Modifier
                .size(100.dp)
                .padding(top = 16.dp)
        )

        Text(
            text = "Crear cuenta",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )


        FormularioTextField(
            value = estado.nombre,
            onValueChange = viewModel::onNombreChange,
            label = "Nombre",
            isError = estado.errores.nombre != null,
            errorMessage = { estado.errores.nombre?.let { Text(it) } }
        )

        FormularioTextField(
            value = estado.correo,
            onValueChange = viewModel::onCorreoChange,
            label = "Correo electrónico",
            isError = estado.errores.correo != null,
            errorMessage = { estado.errores.correo?.let { Text(it) } },
            keyboardType = KeyboardType.Email
        )

        FormularioTextField(
            value = estado.clave,
            onValueChange = viewModel::onClaveChange,
            label = "Contraseña",
            isError = estado.errores.clave != null,
            errorMessage = { estado.errores.clave?.let { Text(it) } },
            isPasswordToggle = true
        )

        FormularioTextField(
            value = estado.direccion,
            onValueChange = viewModel::onDireccionChange,
            label = "Dirección",
            isError = estado.errores.direccion != null,
            errorMessage = { estado.errores.direccion?.let { Text(it) } }
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = estado.aceptaTerminos,
                onCheckedChange = viewModel::onAceptarTerminosChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            )
            Text(
                "Acepto los términos y condiciones",
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Button(
            onClick = {viewModel.registrarUsuario()},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar")
        }
        TextButton(
            onClick = {
                navController.navigate("login") {
                    popUpTo("registro") { inclusive = true }
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("¿Ya tienes una cuenta? Inicia sesion")
        }
    }
}