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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.aplicacion.R
import com.example.aplicacion.ui.Screen.AccentGreen
import com.example.aplicacion.ui.Screen.CardBackground
import com.example.aplicacion.ui.Screen.DarkBackground
import com.example.aplicacion.ui.Screen.TextColor
import com.example.aplicacion.ui.Screen.TextColorSecondary

@Composable
fun TopBar() {
    Column(modifier = Modifier
        .background(DarkBackground)
        .padding(horizontal = 16.dp, vertical = 30.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_level_up),
                contentDescription = "Logo",
                modifier = Modifier.height(30.dp)
            )
            Row {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito", tint = TextColor)
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notificaciones", tint = TextColor)
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar en Level-Up Gamer...", color = TextColorSecondary) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar", tint = TextColorSecondary) },
            shape = CircleShape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentGreen,
                unfocusedBorderColor = Color.DarkGray,
                focusedTextColor = TextColor,
                unfocusedTextColor = TextColor,
                cursorColor = AccentGreen,
                unfocusedContainerColor = CardBackground,
                focusedContainerColor = CardBackground
            )
        )
    }
}