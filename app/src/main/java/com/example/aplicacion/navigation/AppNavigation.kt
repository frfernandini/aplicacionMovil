package com.example.aplicacion.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aplicacion.data.remote.RetrofitInstance
import com.example.aplicacion.model.local.AppDatabase
import com.example.aplicacion.model.repository.ProductoRepository
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
            val factory = RegistroViewModelFactory(LocalContext.current.applicationContext as Application)
            val registroViewModel: RegistroViewModel = viewModel(factory = factory)
            RegistroScreen(navController, registroViewModel)
        }

        composable("login") {
            LoginScreen(navController, loginViewModel, productoViewModel)
        }

        composable("perfil") {
            // ARREGLO: Usar la factory para inyectar el repositorio de preferencias
            val factory = PerfilViewModelFactory(LocalContext.current.applicationContext as Application)
            val perfilViewModel: PerfilViewModel = viewModel(factory = factory)
            ProfileScreen(navController = navController, profileViewModel = perfilViewModel)
        }

        composable("editar_perfil") {
            val factory = EditarPerfilViewModelFactory(LocalContext.current.applicationContext as Application)
            val editarPerfilViewModel: EditarPerfilViewModel = viewModel(factory = factory)
            EditarPerfilScreen(navController = navController, viewModel = editarPerfilViewModel)
        }

        composable("eventos") {
            val factory = EventoViewModelFactory(LocalContext.current.applicationContext as Application)
            val eventoViewModel: EventoViewModel = viewModel(factory = factory)
            EventoScreen(navController, eventoViewModel)
        }

        composable("home") {
            HomeScreen(navController = navController, productoViewModel = productoViewModel)
        }

        composable("carrito") {
            CarritoScreen(
                vm = productoViewModel,
                onBack = { navController.popBackStack() },
                onPagar = { navController.navigate("checkout") }
            )
        }

        composable("checkout") {
            val context = LocalContext.current.applicationContext
            val db = AppDatabase.get(context)
            val apiService = RetrofitInstance.api
            val repo = ProductoRepository(apiService, db.productoDao())

            val checkoutViewModel: CheckoutViewModel = viewModel(factory = CheckoutViewModelFactory(repo))
            val carrito by productoViewModel.carrito.collectAsState()
            val total by productoViewModel.totalCarrito.collectAsState()

            CheckoutScreen(
                navController = navController,
                checkoutViewModel = checkoutViewModel,
                carrito = carrito,
                total = total,
                onPedidoExitoso = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                    productoViewModel.vaciarCarrito()
                }
            )
        }
    }
}
