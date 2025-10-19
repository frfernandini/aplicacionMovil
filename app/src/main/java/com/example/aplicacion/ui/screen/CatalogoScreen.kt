package com.example.aplicacion.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplicacion.model.local.ProductoEntity
import com.example.aplicacion.viewmodel.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    vm: ProductoViewModel,
    onAdd: () -> Unit,
    onEdit: (ProductoEntity) -> Unit
) {
    val productos by vm.productos.collectAsState()


    Scaffold(
        floatingActionButton = { FloatingActionButton(onClick = onAdd) { Text("+") } }
    ) { padding ->
            LazyColumn(Modifier.fillMaxSize().padding(padding)) {
                items(productos) { p ->
                    ListItem(
                        headlineContent = {
                            Text(p.nombre)
                        },
                        supportingContent = {
                            Text("${p.descripcion}")
                        },
                        trailingContent = {
                            Text(String.format("$%.2f", p.precio,), fontSize = 25.sp)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onEdit(p) }
                            .padding(horizontal = 8.dp)
                    )
                }
            }
    }
}