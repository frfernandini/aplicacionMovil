package com.example.aplicacion.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.aplicacion.viewmodel.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoDetailScreen(
    productoViewModel: ProductoViewModel,
    productoId: Long,
    onBack: () -> Unit
) {
    // Llama al ViewModel para que cargue el producto seleccionado
    LaunchedEffect(key1 = productoId) {
        productoViewModel.obtenerProductoPorId(productoId)
    }

    val producto by productoViewModel.productoSeleccionado.collectAsState()
    val carrito by productoViewModel.carrito.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(producto?.nombre ?: "Detalles") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        producto?.let { p ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = p.imagen,
                    contentDescription = p.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Crop
                )
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = p.nombre ?: "Sin nombre",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "$%.2f".format(p.precio ?: 0.0),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = p.descripcion ?: "No hay descripción disponible.",
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    val estaEnCarrito = carrito.any { item -> item.id == p.id }
                    Button(
                        onClick = {
                            if (estaEnCarrito) {
                                productoViewModel.quitarDelCarrito(p)
                            } else {
                                productoViewModel.agregarAlCarrito(p)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (estaEnCarrito) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(if (estaEnCarrito) "Quitar del Carrito" else "Agregar al Carrito", fontSize = 18.sp)
                    }
                }
            }
        } ?: run {
            // Muestra un loader mientras se encuentra el producto
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}
