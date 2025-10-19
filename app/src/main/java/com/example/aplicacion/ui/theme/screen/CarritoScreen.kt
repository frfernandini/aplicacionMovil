package com.example.aplicacion.ui.theme.screen


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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.aplicacion.model.local.ProductoEntity
import com.example.aplicacion.viewModel.ProductoViewModel
import com.example.aplicacion.R
import com.example.aplicacion.ui.theme.azulElectrico
import com.example.aplicacion.ui.theme.grisClaro
import com.example.aplicacion.ui.theme.negroGrafito
import com.example.aplicacion.ui.theme.verdeNeon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(vm: ProductoViewModel, onBack: () -> Unit) {
    val carrito by vm.carrito.collectAsState()
    val total by vm.totalCarrito.collectAsState()

    Scaffold(
        containerColor = negroGrafito,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = negroGrafito),
                title = { Text("Carrito", color = azulElectrico) },
                navigationIcon = { IconButton(onClick = onBack)
                { Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = azulElectrico) } },
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
            if (carrito.isNotEmpty()) {
                BottomAppBar(
                    modifier = Modifier.height(60.dp),
                    containerColor = verdeNeon
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total: $${"%.2f".format(total)}", color = Color.Blue, fontSize = 25.sp)
                        Button(
                            onClick = {
                                //AQUI IRIA LA LOGICA DE PAGO
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = azulElectrico
                            )
                        ) {
                            Text("Pagar")
                        }
                    }
                }
            }
        }

    ) { padding ->
        if (carrito.isEmpty()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("CARRITO VACIO", color = Color.Red)
            }
        } else {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(carrito) { producto ->
                    CarritoItem(
                        producto,
                        onAumentar = { vm.cambiarCantidad(producto, producto.cantidad + 1) },
                        onDisminuir = { vm.cambiarCantidad(producto, producto.cantidad - 1) },
                        onEliminar = { vm.quitarDelCarrito(producto) }
                    )
                }
            }
        }
    }
}

@Composable
fun CarritoItem(
    producto: ProductoEntity,
    onAumentar: () -> Unit,
    onDisminuir: () -> Unit,
    onEliminar: () -> Unit
) {
    val imagenFondo = if (producto.imagen != 0) producto.imagen else R.drawable.ic_launcher_background
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
            Image(
                painter = painterResource(imagenFondo),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(producto.nombre, maxLines = 2, fontSize = 20.sp)
                Text("Precio: $${producto.precio}",fontSize = 16.sp)
                Text("Subtotal: $${"%.2f".format(producto.precio * producto.cantidad)}",fontSize = 16.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDisminuir) { Text("-",fontSize = 25.sp, color = Color.Red) }
                Text("${producto.cantidad}", modifier = Modifier.padding(horizontal = 4.dp), fontSize = 20.sp)
                IconButton(onClick = onAumentar) { Text("+",fontSize = 25.sp, color = verdeNeon) }
                IconButton(onClick = onEliminar) { Icon(Icons.Default.Delete, contentDescription = "Eliminar") }
            }
        }
    }
}