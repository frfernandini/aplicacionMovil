package com.example.aplicacion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels // <-- Importante
import com.example.aplicacion.navigation.AppNavigation
import com.example.aplicacion.ui.theme.AplicacionTheme
import com.example.aplicacion.viewmodel.LoginViewModel
import com.example.aplicacion.viewmodel.LoginViewModelFactory
import com.example.aplicacion.viewmodel.ProductoViewModel
import com.example.aplicacion.viewmodel.ProductoViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Crea las Factories necesarias
        val productoViewModelFactory = ProductoViewModelFactory(application)
        // Crea otras factories que necesites compartir (ej. Login)
        val loginViewModelFactory = LoginViewModelFactory(application)

        // 2. Crea las instancias de los ViewModels LIGADAS A LA ACTIVITY
        val productoViewModel: ProductoViewModel by viewModels { productoViewModelFactory }
        val loginViewModel: LoginViewModel by viewModels { loginViewModelFactory }

        setContent {
            AplicacionTheme {
                // 3. Pasa las instancias creadas a tu navegación
                AppNavigation(
                    productoViewModel = productoViewModel,
                    loginViewModel = loginViewModel
                )
            }
        }
    }
}

