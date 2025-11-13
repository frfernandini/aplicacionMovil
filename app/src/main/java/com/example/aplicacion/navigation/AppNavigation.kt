package com.example.aplicacion.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aplicacion.ui.screen.*
import com.example.aplicacion.viewmodel.*

@Composable
fun AppNavigation(
    productoViewModel: ProductoViewModel,
    loginViewModel: LoginViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("registro") {
            val registroViewModel: RegistroViewModel = viewModel(factory = RegistroViewModelFactory(LocalContext.current.applicationContext as Application))
            RegistroScreen(navController, registroViewModel)
        }
        composable("login") {
            // CORREGIDO: Ahora se pasa el productoViewModel
            LoginScreen(navController, loginViewModel, productoViewModel)
        }
        composable("perfil") {
            val perfilViewModel: PerfilViewModel = viewModel(factory = PerfilViewModelFactory(LocalContext.current.applicationContext as Application))
            ProfileScreen(navController = navController, profileViewModel = perfilViewModel)
        }
        composable("editar_perfil") {
            val editarPerfilViewModel: EditarPerfilViewModel = viewModel(factory = EditarPerfilViewModelFactory(LocalContext.current.applicationContext as Application))
            EditarPerfilScreen(navController = navController, viewModel = editarPerfilViewModel)
        }
        composable("eventos") {
            EventoScreen(navController)
        }
        composable("home") {
            HomeScreen(navController = navController, productoViewModel = productoViewModel)
        }
        composable("carrito") {
            CarritoScreen(vm = productoViewModel, onBack = { navController.popBackStack() })
        }
    }
}
