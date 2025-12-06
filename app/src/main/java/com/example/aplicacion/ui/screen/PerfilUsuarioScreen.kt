package com.example.aplicacion.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplicacion.ui.components.BottomNavBar
import com.example.aplicacion.ui.components.ProfileMenuItem
import com.example.aplicacion.ui.components.UserInfoSection
import com.example.aplicacion.ui.theme.verdeNeon
import com.example.aplicacion.viewmodel.PerfilViewModel

@Composable
fun ProfileScreen(navController: NavController, profileViewModel: PerfilViewModel) {
    val uiState by profileViewModel.perfilState.collectAsState()

    // --- DEBUG LOG ---
    LaunchedEffect(uiState.imagenUri) {
        Log.d("ProfileDebug", "URL de imagen recibida en UI: '${uiState.imagenUri}'")
    }
    // -----------------

    LaunchedEffect(key1 = uiState.logoutExitoso) {
        if (uiState.logoutExitoso) {
            navController.navigate("login") {
                popUpTo(0) { inclusive = true }
            }
            profileViewModel.onNavegacionRealizada()
        }
    }


    Scaffold(
        bottomBar = {
            BottomNavBar(navController,"perfil")
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF121212))
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            UserInfoSection(
                nombreUsuario = uiState.nombreUsuario,
                email = uiState.email,
                imagenUri = uiState.imagenUri
            )

            Spacer(modifier = Modifier.padding(top = 16.dp))


            ProfileMenuItem(Icons.Default.Edit, "Editar Perfil", verdeNeon) {
                navController.navigate("editar_perfil")
            }



            Spacer(modifier = Modifier.weight(1f))


            ProfileMenuItem(Icons.Default.Logout, "Cerrar Sesión", Color.Red) {
                profileViewModel.onCerrarSesion()
            }
        }
    }

}
