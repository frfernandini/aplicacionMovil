package com.example.aplicacion.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplicacion.model.local.ProductoEntity
import com.example.aplicacion.ui.screen.TextColor
import com.example.aplicacion.viewmodel.ProductoViewModel

@Composable
fun ProductSection(
    title: String,
    productos: List<ProductoEntity>,
    vm: ProductoViewModel,
    onEdit: (ProductoEntity) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            color = TextColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),//<- Esto permite agregar 2 productos por fila
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(productos) { producto ->
                ProductCard(
                    producto = producto,
                    vm = vm,
                    onEdit = onEdit
                )
            }
        }
    }
}