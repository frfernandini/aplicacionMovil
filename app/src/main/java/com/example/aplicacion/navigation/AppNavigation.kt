package com.example.aplicacion.navigation
import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aplicacion.ui.Screen.EventoScreen
import com.example.aplicacion.ui.screen.EditarPerfilScreen
import com.example.aplicacion.ui.screen.LoginScreen
import com.example.aplicacion.ui.screen.ProfileScreen
import com.example.aplicacion.ui.screen.RegistroScreen
import com.example.aplicacion.viewmodel.EditarPerfilViewModel
import com.example.aplicacion.viewmodel.EditarPerfilViewModelFactory
import com.example.aplicacion.viewmodel.LoginViewModel
import com.example.aplicacion.viewmodel.LoginViewModelFactory
import com.example.aplicacion.viewmodel.PerfilViewModel
import com.example.aplicacion.viewmodel.PerfilViewModelFactory
import com.example.aplicacion.viewmodel.UsuarioViewModel
import com.example.aplicacion.viewmodel.UsuarioViewModelFactory

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    val context = LocalContext.current
    val application = context.applicationContext as Application

    // Fábricas de ViewModel de la rama 'HEAD'
    val usuarioViewModelFactory = UsuarioViewModelFactory(application)
    val loginViewModelFactory = LoginViewModelFactory(application)
    val editarPerfilViewModelFactory = EditarPerfilViewModelFactory(application)
    val perfilViewModelFactory = PerfilViewModelFactory(application)

    NavHost(
        navController = navController,
        startDestination = "login" // Mantenemos "login" como punto de inicio
    ){
        // Rutas existentes de la rama 'HEAD'
        composable("registro"){
            val usuarioViewModel: UsuarioViewModel = viewModel(factory = usuarioViewModelFactory)
            RegistroScreen(navController,usuarioViewModel)
        }
        composable("login"){
            val loginViewModel: LoginViewModel = viewModel(factory = loginViewModelFactory)
            LoginScreen(navController,loginViewModel)
        }
        composable ("perfil"){
            val perfilViewModel: PerfilViewModel = viewModel(factory = perfilViewModelFactory)
            ProfileScreen(
                navController = navController,
                profileViewModel = perfilViewModel
            )
        }
        composable("editar_perfil"){
            val editarPerfilViewModel: EditarPerfilViewModel = viewModel(factory = editarPerfilViewModelFactory)
            EditarPerfilScreen(
                navController = navController,
                viewModel = editarPerfilViewModel
            )
        }
        composable("eventos") {
            EventoScreen()
        }

    }
}
