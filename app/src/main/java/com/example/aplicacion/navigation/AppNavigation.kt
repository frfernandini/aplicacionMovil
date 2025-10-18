package com.example.aplicacion.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aplicacion.model.local.ProductoEntity
import com.example.aplicacion.ui.theme.screen.FormProductScreen
import com.example.aplicacion.ui.theme.screen.HomeScreen
import com.example.aplicacion.viewModel.ProductoViewModel



@Composable
fun AppNavigation(vm: ProductoViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                navController = navController,
                productoViewModel = vm
            )
        }

        composable("formProducto") {
            FormProductScreen(
                vm = vm,
                onBack = {
                    vm.limpiarFormProd()
                    navController.popBackStack()
                },
                onSaved = {
                    val exito = vm.guardarProducto()
                    if (exito) {
                        navController.popBackStack()
                    }
                },
                onDelete = {
                    // Crear ProductoEntity desde ProductoUiState
                    val productoParaEliminar = ProductoEntity(
                        id = vm.estado.value.id ?: 0,
                        nombre = vm.estado.value.nombre,
                        descripcion = vm.estado.value.descripcion,
                        precio = vm.estado.value.precio.toDoubleOrNull() ?: 0.0,
                        imagen = vm.estado.value.imagen,
                        categoria = vm.estado.value.categoria
                    )

                    vm.eliminarProducto(productoParaEliminar)
                    navController.popBackStack()
                }
            )
        }
    }
}