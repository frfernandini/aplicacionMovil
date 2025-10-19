package com.example.aplicacion.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aplicacion.ui.screen.CarritoScreen
import com.example.aplicacion.ui.screen.HomeScreen
import com.example.aplicacion.viewModel.ProductoViewModel



@Composable
fun AppNavigation(vm: ProductoViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        //HOME
        composable("home") {
            HomeScreen(
                navController = navController,
                productoViewModel = vm
            )
        }
        //FORMULARIO PRODUCTO
        /*composable("formProducto") {
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
                    // Convertir a ProductoEntity
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
        }*/
        //CARRITO
        composable("carrito") {
            CarritoScreen(
                vm = vm,
                onBack = { navController.popBackStack() }
            )
        }
    }
}