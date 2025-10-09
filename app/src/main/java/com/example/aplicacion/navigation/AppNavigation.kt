package com.example.aplicacion.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aplicacion.ui.screen.RegistroScreen
import com.example.aplicacion.ui.screen.ResumenScreen
import com.example.aplicacion.viewmodel.UsuarioViewModel

@Composable
fun AppNavigation(){
    val navController = rememberNavController()


    val usuarioViewModel : UsuarioViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "registro"
    ){
        composable("registro"){
            RegistroScreen(navController,usuarioViewModel)
        }
        composable("resumen"){
            ResumenScreen(usuarioViewModel)
        }

    }

}