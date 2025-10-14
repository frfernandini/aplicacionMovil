package com.example.aplicacion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.aplicacion.navigation.AppNavigation
import com.example.aplicacion.ui.theme.AplicacionTheme
import com.example.aplicacion.viewmodel.UsuarioViewModel
import com.example.aplicacion.viewmodel.UsuarioViewModelFactory

class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AplicacionTheme {
                Scaffold{ innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)){
                        AppNavigation()
                    }
                }
            }
        }
    }
}

