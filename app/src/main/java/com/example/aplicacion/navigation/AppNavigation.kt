package com.example.aplicacion.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.aplicacion.ui.theme.AplicacionTheme
import com.example.aplicacion.ui.Screen.EventoScreen
import androidx.navigation.compose.composable
@Composable
fun AppNavigation(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "eventos"
    ){
        composable("eventos") {
            AplicacionTheme {
                EventoScreen()
            }
        }
    }
}