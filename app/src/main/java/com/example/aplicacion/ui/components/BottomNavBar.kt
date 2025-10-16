package com.example.aplicacion.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.aplicacion.ui.Screen.AccentGreen
import com.example.aplicacion.ui.Screen.CardBackground
import com.example.aplicacion.ui.Screen.TextColorSecondary

@Composable
fun BottomNavBar() {
    NavigationBar(
        containerColor = CardBackground,
        contentColor = TextColorSecondary
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AccentGreen,
                selectedTextColor = AccentGreen,
                unselectedIconColor = TextColorSecondary,
                unselectedTextColor = TextColorSecondary,
                indicatorColor = CardBackground
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = {  },
            icon = { Icon(Icons.Filled.Apps, contentDescription = "Categorías") },
            label = { Text("Categorías") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {  },
            icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "Eventos") },
            label = { Text("Eventos") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {  },
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") }
        )
    }
}