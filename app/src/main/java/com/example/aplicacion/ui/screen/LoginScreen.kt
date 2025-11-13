package com.example.aplicacion.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.aplicacion.ui.components.FormularioTextField
import com.example.aplicacion.viewmodel.LoginViewModel
import com.example.aplicacion.viewmodel.ProductoViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    productoViewModel: ProductoViewModel // <-- ADDED
) {

    val uiState by loginViewModel.loginState.collectAsState()

    // --- THIS IS THE FINAL CONNECTION ---
    LaunchedEffect(key1 = uiState) { // Watch the entire state
        // When login is successful AND we have a userId
        if (uiState.loginExitoso && uiState.userId != null) {
            // 1. Set the user ID in the ProductViewModel
            productoViewModel.setUsuarioId(uiState.userId!!)

            // 2. Navigate to the home screen
            navController.navigate("home") {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
                launchSingleTop = true
            }

            // 3. Reset the login state to prevent re-triggering
            loginViewModel.onNavegacionRealizada()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo_level_up),
            contentDescription = "Logo",
            modifier = Modifier.size(120.dp)
        )

        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            FormularioTextField(
                value = uiState.correo,
                onValueChange = loginViewModel::onCorreoChange,
                label = "Correo electrónico",
                keyboardType = KeyboardType.Email
            )

            FormularioTextField(
                value = uiState.clave,
                onValueChange = loginViewModel::onClaveChange,
                label = "Contraseña",
                isPasswordToggle = true
            )
        }

        uiState.error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Button(
            onClick = { loginViewModel.iniciarSesion() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Entrar")
        }

        TextButton(
            onClick = {
                navController.navigate("registro") {
                    popUpTo("login") { inclusive = true }
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("¿No tienes una cuenta? Registrate")
        }
    }
}
