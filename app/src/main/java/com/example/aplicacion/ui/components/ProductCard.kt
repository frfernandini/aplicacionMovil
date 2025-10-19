package com.example.aplicacion.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.aplicacion.model.local.ProductoEntity
import com.example.aplicacion.ui.screen.CardBackground
import com.example.aplicacion.ui.screen.TextColor
import com.example.aplicacion.R
import com.example.aplicacion.ui.theme.azulElectrico
import com.example.aplicacion.ui.theme.blanco
import com.example.aplicacion.ui.theme.negroGrafito
import com.example.aplicacion.ui.screen.AccentGreen
import com.example.aplicacion.ui.theme.verdeDarkBrillante
import com.example.aplicacion.ui.theme.verdeNeon
import com.example.aplicacion.ui.theme.verdeOscuro
import com.example.aplicacion.viewmodel.ProductoViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProductCard(
    producto: ProductoEntity,
    vm: ProductoViewModel,
    onEdit: (ProductoEntity) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = verdeDarkBrillante,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        val imagenFondo = if (producto.imagen != 0) producto.imagen else R.drawable.ic_launcher_background

        Image(
            painter = painterResource(id = imagenFondo),
            contentDescription = producto.nombre,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(8.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Fit
        )

        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
            Text(
                text = producto.nombre,
                fontSize = 15.sp,
                color = azulElectrico,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = producto.descripcion,
                color = TextColor,
                fontWeight = FontWeight.SemiBold,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "$${String.format("%.2f", producto.precio)}",
                color = verdeNeon,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            //ANIMACION BOTON
            val scope = rememberCoroutineScope()
            var press by remember { mutableStateOf(false) }

            val scale by animateFloatAsState(
                targetValue = if (press) 0.9f else 1f,
                animationSpec = tween(400)
            )

            Button(
                onClick = {
                    //CAMBIAR EL ESTADO PARA ANIMARLO
                    press = true
                    //ACCION DE CARRITO
                    vm.modificarCarrito(producto)
                    //REINICIA EL TAMAÑO DESPUES DE 400ms
                    scope.launch {
                        delay(400)
                        press = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer(scaleX = scale, scaleY = scale),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (producto.enCarrito) Color.Red else AccentGreen
                )
            ) {
                Text(
                    text = if (producto.enCarrito) "Quitar del carrito" else "Agregar al carrito",
                    color = if (producto.enCarrito) blanco else negroGrafito
                )
            }
        }
    }
}
