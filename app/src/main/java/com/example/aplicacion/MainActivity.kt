package com.example.aplicacion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.aplicacion.navigation.AppNavigation
import com.example.aplicacion.ui.theme.AplicacionTheme
import com.example.aplicacion.viewmodel.LoginViewModel
import com.example.aplicacion.viewmodel.LoginViewModelFactory
import com.example.aplicacion.viewmodel.ProductoViewModel
import com.example.aplicacion.viewmodel.ProductoViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val productoViewModelFactory = ProductoViewModelFactory(application)

        val loginViewModelFactory = LoginViewModelFactory(application)


        val productoViewModel: ProductoViewModel by viewModels { productoViewModelFactory }
        val loginViewModel: LoginViewModel by viewModels { loginViewModelFactory }

        setContent {
            AplicacionTheme {

                AppNavigation(
                    productoViewModel = productoViewModel,
                    loginViewModel = loginViewModel
                )
            }
        }
    }
}

