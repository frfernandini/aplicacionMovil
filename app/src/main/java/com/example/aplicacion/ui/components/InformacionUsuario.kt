package com.example.aplicacion.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.example.aplicacion.R


@Composable
fun UserInfoSection(
    nombreUsuario: String,
    email: String,
    imagenUri:String
) {
    Spacer(modifier = Modifier.height(32.dp))
    // Avatar
    Image(
        painter = rememberAsyncImagePainter(
            model = if (imagenUri.isNotEmpty()) {
                imagenUri.toUri() // Carga la URI del usuario si existe
            } else {
                R.drawable.logo_level_up // Muestra el logo por defecto si no hay foto
            }
        ),
        contentDescription = "Avatar de usuario",
        contentScale = ContentScale.Crop, // Asegura que la imagen llene el círculo
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape) // Corta la imagen en forma de círculo
            .border(2.dp, Color(0xFF00BFFF), CircleShape)
    )
    Spacer(modifier = Modifier.height(16.dp))

    // 2. Usa los parámetros en los Text
    Text(text = nombreUsuario, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
    Text(text = email, fontSize = 16.sp, color = Color.Gray)

    // Los datos estáticos pueden permanecer igual
    Spacer(modifier = Modifier.height(24.dp))
    Text("Nivel 15", fontSize = 18.sp, color = Color.White)
    Text("12.500 Puntos LevelUp", fontSize = 18.sp, color = Color.White)
    Spacer(modifier = Modifier.height(32.dp))
}