package com.example.aplicacion.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplicacion.model.local.ProductoEntity
import com.example.aplicacion.ui.theme.screen.CardBackground
import com.example.aplicacion.ui.theme.screen.TextColor
import com.example.aplicacion.R
import com.example.aplicacion.ui.theme.screen.AccentGreen
import com.example.aplicacion.viewModel.ProductoViewModel

@Composable
fun ProductCard(
    producto: ProductoEntity,
    vm: ProductoViewModel,
    onEdit: (ProductoEntity) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEdit(producto) }
            .border(
                width = 2.dp,
                color = AccentGreen,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {

        //EN CASO DE SER QUE LA IMAGEN SEA 0(NO ALLA UNA IMAGEN)
        //SE LE DEFINIRA UN FONDO POR DEFECTO PARA EVITAR CRASHEO
        val imagenFondo = if (producto.imagen != 0)
            producto.imagen
        else
            R.drawable.ic_launcher_background

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
                color = TextColor,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = producto.descripcion,
                color = TextColor,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "$${String.format("%.2f", producto.precio)}",
                color = TextColor,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    if (producto.enCarrito) {
                        vm.quitarDelCarrito(producto)
                    } else {
                        vm.agregarAlCarrito(producto)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (producto.enCarrito) Color.Red else AccentGreen
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (producto.enCarrito) "Quitar del carrito" else "Agregar al carrito",
                    color = Color.White
                )
            }
        }
    }
}
