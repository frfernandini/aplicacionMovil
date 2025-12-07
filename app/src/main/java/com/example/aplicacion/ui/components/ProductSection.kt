package com.example.aplicacion.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplicacion.data.remote.dto.ProductoDto
import com.example.aplicacion.ui.screen.TextColor
import com.example.aplicacion.viewmodel.ProductoViewModel

@Composable
fun ProductSection(
    title: String,
    productos: List<ProductoDto>,
    vm: ProductoViewModel,
    carrito: List<ProductoDto>,
    onEdit: (Int) -> Unit,
    onProductClick: (Long) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextColor,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            // ARREGLO: height() es parte de Modifier
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .height(500.dp) 
        ) {
            items(productos) {
                val estaEnCarrito = carrito.any { item -> item.id == it.id }
                ProductCard(
                    producto = it,
                    vm = vm,
                    estaEnCarrito = estaEnCarrito,
                    onProductClick = onProductClick
                )
            }
        }
    }
}