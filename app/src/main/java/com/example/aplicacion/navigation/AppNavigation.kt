package com.example.aplicacion.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aplicacion.ui.screen.LoginScreen
import com.example.aplicacion.ui.screen.RegistroScreen
import com.example.aplicacion.ui.screen.ResumenScreen
import com.example.aplicacion.viewmodel.LoginViewModel
import com.example.aplicacion.viewmodel.LoginViewModelFactory
import com.example.aplicacion.viewmodel.UsuarioViewModel
import com.example.aplicacion.viewmodel.UsuarioViewModelFactory

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val usuarioViewModelFactory = UsuarioViewModelFactory(application)
    val loginViewModelFactory = LoginViewModelFactory(application)

    NavHost(
        navController = navController,
        startDestination = "login"
    ){
        composable("registro"){
            val usuarioViewModel: UsuarioViewModel = viewModel(factory = usuarioViewModelFactory)
            RegistroScreen(navController,usuarioViewModel)
        }
        composable("resumen"){
            val usuarioViewModel: UsuarioViewModel = viewModel(factory = usuarioViewModelFactory)
            ResumenScreen(usuarioViewModel)
        }
        composable("login"){
            val loginViewModel: LoginViewModel = viewModel(factory = loginViewModelFactory)
            LoginScreen(navController,loginViewModel)
        }
    }

}