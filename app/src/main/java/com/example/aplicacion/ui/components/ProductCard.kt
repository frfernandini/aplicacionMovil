package com.example.aplicacion.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.aplicacion.R
import com.example.aplicacion.data.remote.dto.ProductoDto
import com.example.aplicacion.ui.screen.AccentGreen
import com.example.aplicacion.ui.screen.CardBackground
import com.example.aplicacion.ui.screen.TextColor
import com.example.aplicacion.ui.theme.azulElectrico
import com.example.aplicacion.ui.theme.blanco
import com.example.aplicacion.ui.theme.negroGrafito
import com.example.aplicacion.ui.theme.verdeDarkBrillante
import com.example.aplicacion.ui.theme.verdeNeon
import com.example.aplicacion.viewmodel.ProductoViewModel

@Composable
fun ProductCard(
    producto: ProductoDto,
    vm: ProductoViewModel,
    estaEnCarrito: Boolean,
    onProductClick: (Long) -> Unit
) {
    // ARREGLO: Definir altura fija para la tarjeta para asegurar uniformidad en la grilla
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(360.dp) // Altura fija y suficiente para evitar cortes
            .border(
                width = 2.dp,
                color = verdeDarkBrillante,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onProductClick(producto.id) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        val imageUrlCompleta = producto.imagen

        AsyncImage(
            model = imageUrlCompleta,
            contentDescription = producto.nombre,
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp) // Imagen con tamaño fijo y consistente
                .padding(8.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Fit,
            error = painterResource(id = R.drawable.logo_level_up)
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // SECCIÓN SUPERIOR: Texto (Título y Descripción)
            // Usamos weight(1f) para que ocupe el espacio disponible sin empujar el botón
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre ?: "Nombre no disponible",
                    fontSize = 15.sp,
                    color = azulElectrico,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = producto.descripcion ?: "Sin descripción",
                    color = TextColor,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            // SECCIÓN INFERIOR: Precio y Botón
            // Esta sección siempre se quedará pegada al fondo
            Column {
                Text(
                    text = "$%.2f".format(producto.precio ?: 0.0),
                    color = verdeNeon,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                val scope = rememberCoroutineScope()
                var press by remember { mutableStateOf(false) }

                val scale by animateFloatAsState(
                    targetValue = if (press) 0.9f else 1f,
                    animationSpec = tween(400)
                )

                Button(
                    onClick = {
                        if (estaEnCarrito) {
                            vm.quitarDelCarrito(producto)
                        } else {
                            vm.agregarAlCarrito(producto)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp) // Altura fija para el botón: se acabó el estiramiento
                        .graphicsLayer(scaleX = scale, scaleY = scale),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (estaEnCarrito) Color.Red else AccentGreen
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (estaEnCarrito) "Quitar" else "Agregar",
                        color = if (estaEnCarrito) blanco else negroGrafito,
                        fontSize = 14.sp,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
