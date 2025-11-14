package com.example.aplicacion.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.aplicacion.R
import com.example.aplicacion.data.remote.dto.ProductoDto
import com.example.aplicacion.ui.components.Loader
import com.example.aplicacion.ui.theme.azulElectrico
import com.example.aplicacion.ui.theme.grisClaro
import com.example.aplicacion.ui.theme.negroGrafito
import com.example.aplicacion.viewmodel.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    vm: ProductoViewModel,
    onBack: () -> Unit,
    onPagar: () -> Unit
) {
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
                }
            )
        },
        bottomBar = {
            if (!isLoading && carrito.isNotEmpty()) {
                BottomAppBar(
                    modifier = Modifier.height(60.dp),
                    containerColor = Color.DarkGray
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total: $${"%.2f".format(total)}", color = azulElectrico, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                        Button(
                            onClick = onPagar,
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
                    Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("CARRITO VACIO", color = Color.Red, fontSize = 20.sp)
                }
            } else {
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    items(carrito) { producto ->
                        CarritoItem(
                            producto = producto,
                            onAumentar = { vm.aumentarCantidad(producto) },
                            onDisminuir = { vm.disminuirCantidad(producto) },
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
    producto: ProductoDto,
    onAumentar: () -> Unit,
    onDisminuir: () -> Unit,
    onEliminar: () -> Unit
) {
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

            Column(Modifier.weight(1f)) {
                Text(producto.nombre ?: "Sin nombre", maxLines = 2, fontSize = 20.sp, color = azulElectrico, fontWeight = FontWeight.Bold)
                Text("Precio: $${"%.2f".format(producto.precio ?: 0.0)}", fontSize = 16.sp, color = negroGrafito)

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    // --- CORRECCIÓN ESTÉTICA: Color más visible ---
                    IconButton(onClick = onDisminuir, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Remove, contentDescription = "Disminuir cantidad", tint = azulElectrico)
                    }
                    Text("${producto.cantidad}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = negroGrafito, modifier = Modifier.padding(horizontal = 8.dp))
                    IconButton(onClick = onAumentar, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Add, contentDescription = "Aumentar cantidad", tint = azulElectrico)
                    }
                }
            }

            IconButton(onClick = onEliminar) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
            }
        }
    }
}
