package com.example.aplicacion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.aplicacion.ui.theme.screen.FormProductScreen
import com.example.aplicacion.viewModel.ProductoViewModel
import com.example.aplicacion.viewModel.ProductoViewModelFactory

class MainActivity : ComponentActivity() {

    private val vm: ProductoViewModel by viewModels {
        ProductoViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //FormProductScreen(vm)
        }
    }
}
