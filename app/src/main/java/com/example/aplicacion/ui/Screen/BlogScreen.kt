package com.example.aplicacion.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.aplicacion.R
import com.example.aplicacion.data.remote.dto.BlogDto
import com.example.aplicacion.ui.components.BottomNavBar
import com.example.aplicacion.ui.components.Loader
import com.example.aplicacion.ui.components.TopBar
import com.example.aplicacion.viewmodel.BlogViewModel

@Composable
fun BlogScreen(navController: NavController, viewModel: BlogViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopBar(navController) },
        bottomBar = { BottomNavBar(navController, "blogs") },
        containerColor = DarkBackground // Reutilizamos el fondo oscuro del Home
    ) { padding ->
        if (uiState.isLoading) {
            Loader()
        } else if (uiState.error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.error ?: "Error desconocido",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    Text(
                        text = "Últimas Noticias",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextColor,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                if (uiState.blogs.isEmpty()) {
                    item {
                        Text(
                            text = "No hay noticias publicadas aún.",
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 32.dp)
                        )
                    }
                } else {
                    items(uiState.blogs) { blog ->
                        BlogCard(blog = blog)
                    }
                }
            }
        }
    }
}

@Composable
fun BlogCard(blog: BlogDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(), // Altura dinámica según el contenido
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground) // Reutilizamos color de tarjeta
    ) {
        Column {
            AsyncImage(
                model = blog.imagenUrl,
                contentDescription = blog.titulo,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp), // Altura fija para la imagen para dar impacto visual
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.logo_level_up), // Mientras carga
                error = painterResource(id = R.drawable.logo_level_up) // Si falla o es nula
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = blog.titulo ?: "Sin título", // Protección
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = AccentGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = blog.autor ?: "LevelUp Team",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.LightGray
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = AccentGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    // ARREGLO: Manejar el caso en que la fecha sea nula
                    Text(
                        text = blog.fecha?.split("T")?.firstOrNull() ?: "Fecha no disponible",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.LightGray
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = blog.contenido ?: "Contenido no disponible", // Protección
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextColor.copy(alpha = 0.8f),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 20.sp
                )
            }
        }
    }
}
