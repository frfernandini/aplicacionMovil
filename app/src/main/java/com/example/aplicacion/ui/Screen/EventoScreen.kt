package com.example.aplicacion.ui.Screen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aplicacion.R
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.example.aplicacion.model.Evento
import com.example.aplicacion.ui.components.BottomNavBar

val listaDeEventos = listOf(
    Evento(
        nombre = "Torneo 1",
        fecha = "25 de Octubre, 2025",
        lugar = "DUOCUC",
        imagenResId = R.drawable.logo_level_up, // Reemplaza con tus imágenes
        ubicacion = LatLng(-33.044411259247035, -71.61555125302814) // Buenos Aires
    ),
    Evento(
        nombre = "Evento 1",
        fecha = "15 de Noviembre, 2025",
        lugar = "DUOCUC",
        imagenResId = R.drawable.logo_level_up, // Reemplaza con tus imágenes
        ubicacion = LatLng(-33.044411259247035, -71.61555125302814) // Nueva York
    ),
    Evento(
        nombre = "Torneo 3",
        fecha = "5 de Diciembre, 2025",
        lugar = "DUOCUC",
        imagenResId = R.drawable.logo_level_up, // Reemplaza con tus imágenes
        ubicacion = LatLng(-33.044411259247035, -71.61555125302814) // París
    )
)


@Composable
fun EventoScreen() {
    val ubicacionInicial = LatLng(-33.044411259247035, -71.61555125302814) // Centrar el mapa inicialmente

    // Estado para controlar la cámara del mapa
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(ubicacionInicial, 1f) // Zoom lejano para ver el mundo
    }

    Scaffold(
        bottomBar = {
            // 2. Colocamos tu BottomNavBar en la parte inferior
            BottomNavBar()
        }
    ) { innerPadding -> // El contenido de la pantalla va aquí dentro

        // 3. El Column principal ahora usa el padding del Scaffold
        Column(
            modifier = Modifier
                .fillMaxSize()
                // Esto asegura que tu contenido no se superponga con la barra de navegación
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // --- SECCIÓN DEL MAPA (sin cambios) ---
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f),
                cameraPositionState = cameraPositionState
            ) {
                listaDeEventos.forEach { evento ->
                    Marker(
                        state = MarkerState(position = evento.ubicacion),
                        title = evento.nombre,
                        snippet = evento.lugar
                    )
                }
            }


            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Próximos Eventos",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                items(listaDeEventos) { evento ->
                    EventoCard(evento = evento)
                }
            }
        }
    }
}


@Composable
fun EventoCard(evento: Evento) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = evento.imagenResId),
                contentDescription = "Imagen del evento ${evento.nombre}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f) // Permite que la columna ocupe el espacio restante
            ) {
                Text(
                    text = evento.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                InfoRow(icon = Icons.Default.CalendarToday, text = evento.fecha)
                Spacer(modifier = Modifier.height(4.dp))
                InfoRow(icon = Icons.Default.Place, text = evento.lugar)
            }
        }
    }
}


@Composable
private fun InfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null, // Decorativo
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@Preview(showBackground = true)
@Composable
fun EventoScreenPreview() {
    EventoScreen()

}