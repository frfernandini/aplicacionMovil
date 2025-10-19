package com.example.aplicacion.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
@Composable
fun BottomNavBar(navController : NavController) {
    var selectedItem by remember { mutableStateOf(0) }

    NavigationBar(

        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        NavigationBarItem(
            selected = selectedItem == 0,
            onClick = {
                selectedItem = 0
                navController.navigate("home")
            },
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.secondary,
                selectedTextColor = MaterialTheme.colorScheme.secondary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                indicatorColor = MaterialTheme.colorScheme.surface
            )
        )
        NavigationBarItem(
            selected = selectedItem == 1,
            onClick = {
                selectedItem = 1
                navController.navigate("eventos")      },
            icon = { Icon(Icons.Default.Star, contentDescription = "Eventos") },
            label = { Text("Eventos") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.secondary,
                selectedTextColor = MaterialTheme.colorScheme.secondary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                indicatorColor = MaterialTheme.colorScheme.surface
            )
        )
        NavigationBarItem(
            selected = selectedItem == 2,
            onClick = {
                selectedItem = 2
                navController.navigate("perfil")
            },
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.secondary,
                selectedTextColor = MaterialTheme.colorScheme.secondary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                indicatorColor = MaterialTheme.colorScheme.surface
            )
        )
    }
}
