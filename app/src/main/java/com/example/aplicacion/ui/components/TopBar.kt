package com.example.aplicacion.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplicacion.R
import com.example.aplicacion.ui.screen.DarkBackground
import com.example.aplicacion.ui.screen.TextColor

@Composable
fun TopBar(
    navController: NavController
) {
    Column(modifier = Modifier
        .background(DarkBackground)
        .padding(horizontal = 16.dp, vertical = 15.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_level_up),
                contentDescription = "Logo",
                modifier = Modifier.height(20.dp)
            )
            Row {
                // --- CORREGIDO ---
                IconButton(onClick = { navController.navigate("carrito") }) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito", tint = TextColor)
                }
                IconButton(onClick = { /* TODO: Implementar navegación a notificaciones */ }) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notificaciones", tint = TextColor)
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}
