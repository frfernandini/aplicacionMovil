package com.example.aplicacion.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.error
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.aplicacion.R
import com.example.aplicacion.ui.components.FormularioTextField // <-- ¡REUTILIZANDO!
import com.example.aplicacion.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel() // Obtenemos el ViewModel
) {
    // Observamos el estado desde el ViewModel
    val uiState by loginViewModel.loginState.collectAsState()

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
            onClick = {
                loginViewModel.iniciarSesion()

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Entrar")
        }
    }
}
