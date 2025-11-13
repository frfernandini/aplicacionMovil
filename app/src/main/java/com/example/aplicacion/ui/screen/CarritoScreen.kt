package com.example.aplicacion.ui.screen

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.aplicacion.R
import com.example.aplicacion.data.remote.dto.ProductoDto
import com.example.aplicacion.ui.components.Loader
import com.example.aplicacion.ui.theme.azulElectrico
import com.example.aplicacion.ui.theme.grisClaro
import com.example.aplicacion.ui.theme.negroGrafito
import com.example.aplicacion.viewmodel.ProductoViewModel
import com.example.aplicacion.viewmodel.ProductoViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    vm: ProductoViewModel,
    onBack: () -> Unit
) {
    // Esta parte ya está bien, porque el ViewModel ahora expone la lista de DTOs.
    val carrito by vm.carrito.collectAsState()
    val total by vm.totalCarrito.collectAsState()
    val isLoading by vm.isLoading.collectAsState()

    Scaffold(
        containerColor = negroGrafito,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = negroGrafito),
                title = { Text("Carrito", color = azulElectrico) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = azulElectrico)
                    }
                },
                actions = {
                    if (carrito.isNotEmpty()) {
                        IconButton(onClick = { vm.vaciarCarrito() }) {
                            Icon(Icons.Default.Delete, contentDescription = "Vaciar carrito", tint = Color.Red)
                        }
                    }
                },
            )
        },
        bottomBar = {
            if (!isLoading && carrito.isNotEmpty()) {
                BottomAppBar(
                    modifier = Modifier.height(60.dp),
                    containerColor = Color.DarkGray
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // El total ya se calcula correctamente en el ViewModel
                        Text("Total: $${"%.2f".format(total)}", color = azulElectrico, fontSize = 25.sp)
                        Button(
                            onClick = { /* Lógica de pago futura */ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                        ) {
                            Text("Pagar")
                        }
                    }
                }
            }
        }
    ) { padding ->
        if (isLoading) {
            Loader()
        } else {
            if (carrito.isEmpty()) {
                Box(
                    Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("CARRITO VACIO", color = Color.Red)
                }
            } else {
                LazyColumn(
                    Modifier.fillMaxSize().padding(padding)
                ) {
                    items(carrito) { producto -> // 'producto' ahora es de tipo ProductoDto
                        // ¡ARREGLADO! Llamamos a la nueva versión de CarritoItem
                        CarritoItem(
                            producto = producto,
                            onEliminar = { vm.quitarDelCarrito(producto) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CarritoItem(
    producto: ProductoDto, // <-- ¡CAMBIADO! Ahora recibe un ProductoDto
    onEliminar: () -> Unit
) {
    // --- LÓGICA DE IMAGEN ACTUALIZADA ---
    // Construye la URL completa a partir de la ruta que viene del backend
    val imageUrlCompleta = "http://192.168.100.14:8080${producto.imagen}"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = grisClaro)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Usa AsyncImage de Coil para cargar la imagen desde la URL
            AsyncImage(
                model = imageUrlCompleta,
                contentDescription = producto.nombre,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.logo_level_up)
            )

            Spacer(Modifier.width(12.dp))

            // --- LÓGICA DE TEXTO ACTUALIZADA ---
            Column(Modifier.weight(1f)) {
                Text(producto.nombre ?: "Sin nombre", maxLines = 2, fontSize = 20.sp, color = azulElectrico)
                // Se usa '?: 0.0' para manejar precios nulos de forma segura
                Text("Precio: $${"%.2f".format(producto.precio ?: 0.0)}", fontSize = 16.sp, color = negroGrafito)
                // Se elimina la línea de subtotal y la cantidad, ya que el DTO no las tiene.
            }

            // Mantenemos solo el botón de eliminar, ya que la lógica de cantidad fue simplificada.
            IconButton(onClick = onEliminar) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.DarkGray)
            }
        }
    }
}
