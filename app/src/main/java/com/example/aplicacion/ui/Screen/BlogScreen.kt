package com.example.aplicacion.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
        containerColor = DarkBackground
    ) { padding ->
        if (uiState.isLoading) {
            Loader()
        } else if (uiState.error != null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text(text = uiState.error ?: "Error desconocido", color = Color.Red)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Últimas Noticias",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp),
                        color = TextColor
                    )
                }
                items(uiState.blogs) { blog ->
                    BlogCard(blog = blog)
                }
            }
        }
    }
}

@Composable
fun BlogCard(blog: BlogDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column {
            blog.imagenUrl?.let {
                AsyncImage(
                    model = it,
                    contentDescription = blog.titulo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.logo_level_up)
                )
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = blog.titulo,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Por ${blog.autor ?: "Anónimo"} | ${blog.fecha}",
                    style = MaterialTheme.typography.labelMedium,
                    color = AccentGreen
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = blog.contenido,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextColor,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}