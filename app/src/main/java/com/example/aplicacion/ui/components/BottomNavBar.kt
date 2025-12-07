package com.example.aplicacion.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
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
import androidx.navigation.NavController

@Composable
fun BottomNavBar(navController : NavController, currentRoute:String?) {

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = {
                if (currentRoute != "home") {
                    navController.navigate("home")
                }
            },
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.secondary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                indicatorColor = MaterialTheme.colorScheme.surface
            )
        )
        
        // NUEVO ÍTEM: BLOGS
        NavigationBarItem(
            selected = currentRoute == "blogs",
            onClick = {
                if (currentRoute != "blogs") {
                    navController.navigate("blogs")
                }
            },
            icon = { Icon(Icons.Default.Article, contentDescription = "Blogs") },
            label = { Text("Noticias") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.secondary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                indicatorColor = MaterialTheme.colorScheme.surface
            )
        )

        NavigationBarItem(
            selected = currentRoute == "eventos",
            onClick = {
                if (currentRoute != "eventos") {
                    navController.navigate("eventos")
                }
            },
            icon = { Icon(Icons.Default.Star, contentDescription = "Eventos") },
            label = { Text("Eventos") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.secondary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                indicatorColor = MaterialTheme.colorScheme.surface
            )
        )
        NavigationBarItem(
            selected = currentRoute == "perfil",
            onClick = {
                if (currentRoute != "perfil") {
                    navController.navigate("perfil")
                }
            },
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.secondary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                indicatorColor = MaterialTheme.colorScheme.surface
            )
        )
    }
}
