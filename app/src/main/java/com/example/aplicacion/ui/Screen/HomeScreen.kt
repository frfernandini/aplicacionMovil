package com.example.aplicacion.ui.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.aplicacion.R
import com.example.aplicacion.model.Producto
import com.example.aplicacion.ui.components.BottomNavBar
import com.example.aplicacion.ui.components.ProductSection
import com.example.aplicacion.ui.components.TopBar
// --- Paleta de Colores y Tema (Basado en la Imagen) ---
val DarkBackground = Color(0xFF1f1f1f)
val CardBackground = Color(0xFF1E1E1E)
val AccentGreen = Color(0xFF39FF14)
val AccentBlue = Color(0xFF1E90FF)
val TextColor = Color.White
val TextColorSecondary = Color.Gray





@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomNavBar() },
        containerColor = DarkBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            BannerCarousel()

            ProductSection(
                title = "Recomendado para ti",
                productos = listOf(
                    Producto("Teclado Mecánico RGB", R.drawable.ic_launcher_background, 4.5f, 31, 29.00, "28% OFF", AccentBlue),
                    Producto("VR Headset Pro", R.drawable.ic_launcher_background, 4.8f, 52, 199.00, "28% OFF", AccentGreen)
                )
            )


            ProductSection(
                title = "Ofertas de la Semana",
                productos = listOf(
                    Producto("Mouse Gamer PRO-X", R.drawable.ic_launcher_background, 4.7f, 89, 45.00, "30% OFF", AccentGreen),
                    Producto("Silla Gamer Ergonómica", R.drawable.ic_launcher_background, 4.9f, 120, 250.00, "20% OFF", AccentGreen)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}





@Composable
fun BannerCarousel() {
    Image(
        painter = painterResource(id = R.drawable.imagen_banner_1),
        contentDescription = "Banner Promocional",
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp)),
        contentScale = ContentScale.Crop
    )
}






@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen(navController = rememberNavController())
    }
}