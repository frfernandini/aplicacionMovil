package com.example.aplicacion.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aplicacion.ui.Screen.HomeScreen // Asegúrate de que el import sea correcto

@Composable
fun AppNavigation() {
    // 1. Crea un controlador de navegación. Este se encarga de gestionar
    //    el historial de pantallas (el "back stack").
    val navController = rememberNavController()

    // 2. Define el NavHost, que es el contenedor donde se mostrarán
    //    tus diferentes pantallas (composables).
    NavHost(
        navController = navController,
        startDestination = "home" // 3. Especifica la "ruta" de la pantalla inicial.
    ) {
        // 4. Define cada pantalla (composable) y asígnale una ruta única.
        composable("home") {
            // Cuando la ruta sea "home", se mostrará este Composable.
            HomeScreen(navController = navController)
        }

        // Aquí añadirás las otras pantallas más adelante, por ejemplo:
        /*
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("registro") {
            RegistroScreen(navController = navController)
        }
        composable("perfil") {
            ProfileScreen(navController = navController)
        }
        */
    }
}