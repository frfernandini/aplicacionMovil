package com.example.aplicacion.ui.screen

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplicacion.R
import com.example.aplicacion.model.Evento
import com.example.aplicacion.ui.components.BottomNavBar
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.google.android.gms.maps.CameraUpdateFactory


val listaDeEventos = listOf(
    Evento(
        nombre = "Torneo 1",
        fecha = "25 de Octubre, 2025",
        lugar = "DUOCUC",
        imagenResId = R.drawable.logo_level_up,
        ubicacion = LatLng(-33.044411259247035, -71.61555125302814)
    ),
    Evento(
        nombre = "Evento 1",
        fecha = "15 de Noviembre, 2025",
        lugar = "DUOCUC",
        imagenResId = R.drawable.logo_level_up,
        ubicacion = LatLng(-33.044411259247035, -71.61555125302814)
    ),
    Evento(
        nombre = "Torneo 3",
        fecha = "5 de Diciembre, 2025",
        lugar = "DUOCUC",
        imagenResId = R.drawable.logo_level_up,
        ubicacion = LatLng(-33.044411259247035, -71.61555125302814)
    )
)

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class) // Anotaciones combinadas
@Composable
fun EventoScreen(navController: NavController) {

    // --- LÓGICA DE PERMISOS Y UBICACIÓN (SIN CAMBIOS) ---
    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    var ubicacionActual by remember { mutableStateOf<LatLng?>(null) }
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        locationPermissionsState.launchMultiplePermissionRequest()
    }


    @SuppressLint("MissingPermission")
    LaunchedEffect(locationPermissionsState.allPermissionsGranted) {
        if (locationPermissionsState.allPermissionsGranted) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    ubicacionActual = LatLng(location.latitude, location.longitude)
                }
            }
        }
    }


    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-33.0444, -71.6155), 10f)
    }

    LaunchedEffect(ubicacionActual) {
        ubicacionActual?.let {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(it,15f),
                durationMs = 1000
            )
        }
    }

    Scaffold(
        bottomBar = {

            BottomNavBar(navController,"eventos")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // --- MODIFICACIÓN CLAVE ---
            // El GoogleMap ahora está fuera del `if`. Siempre se mostrará.
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f),
                cameraPositionState = cameraPositionState,
                // Las propiedades del mapa ahora dependen de si los permisos fueron concedidos.
                properties = MapProperties(
                    isMyLocationEnabled = locationPermissionsState.allPermissionsGranted
                ),
                uiSettings = MapUiSettings(
                    myLocationButtonEnabled = locationPermissionsState.allPermissionsGranted
                )
            ) {
                // Los marcadores se muestran siempre, como antes.
                listaDeEventos.forEach { evento ->
                    Marker(
                        state = MarkerState(position = evento.ubicacion),
                        title = evento.nombre,
                        snippet = evento.lugar
                    )
                }
            }

            // La lista de eventos se mantiene igual
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
                modifier = Modifier.weight(1f)
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
            contentDescription = null,
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



