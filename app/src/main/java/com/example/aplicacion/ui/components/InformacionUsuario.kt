package com.example.aplicacion.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.aplicacion.R

@Composable
fun UserInfoSection(
    nombreUsuario: String,
    email: String,
    imagenUri: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // ARREGLO: Usar AsyncImage que maneja URLs de internet y URIs locales de forma nativa.
        AsyncImage(
            model = imagenUri.ifEmpty { R.drawable.logo_level_up },
            contentDescription = "Avatar de usuario",
            placeholder = painterResource(id = R.drawable.logo_level_up), // Opcional: muestra esto mientras carga
            error = painterResource(id = R.drawable.logo_level_up), // Muestra esto si la carga falla
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(2.dp, Color(0xFF00BFFF), CircleShape)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = nombreUsuario, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(text = email, fontSize = 16.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))
        Text("Nivel 15", fontSize = 18.sp, color = Color.White)
        Text("12.500 Puntos LevelUp", fontSize = 18.sp, color = Color.White)
        Spacer(modifier = Modifier.height(32.dp))
    }
}