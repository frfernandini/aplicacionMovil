package com.example.aplicacion.ui.theme.screen


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplicacion.R
import com.example.aplicacion.model.local.ProductoEntity
import com.example.aplicacion.model.local.listaProductosEstaticos
import com.example.aplicacion.ui.components.ProductSection
import com.example.aplicacion.ui.components.TopBar
import com.example.aplicacion.ui.theme.azulElectrico
import com.example.aplicacion.ui.theme.components.BottomNavBar
import com.example.aplicacion.ui.theme.grisClaro
import com.example.aplicacion.ui.theme.negroGrafito
import com.example.aplicacion.ui.theme.verdeNeon
import com.example.aplicacion.viewModel.ProductoViewModel

// --- Paleta de Colores y Tema (Basado en la Imagen) ---
val DarkBackground = Color(0xFF1f1f1f)
val CardBackground = Color(0xFF1E1E1E)
val AccentGreen = Color(0xFF39FF14)
val AccentBlue = Color(0xFF1E90FF)
val TextColor = Color.White
val TextColorSecondary = Color.Gray


@Composable
fun HomeScreen(
    navController: NavController,
    productoViewModel: ProductoViewModel
) {
    val productos by productoViewModel.catalogo.collectAsState()
    val categoriaSeleccionada by productoViewModel.busquedaCategoria.collectAsState()

    val filtradoProductos =
        if (categoriaSeleccionada.isEmpty())//<- Devolvera la lista completa en caso de que no alla una categoria seleccionada
            productos
        else
            productos.filter { it.categoria == categoriaSeleccionada }//<- Aplicara el filtro correspondiente

    Scaffold(
        topBar = { TopBar(navController = navController) },
        bottomBar = { BottomNavBar() },
        containerColor = DarkBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            BannerCarousel()

            //BOTONES DE CATEGORIA
            Categoria(
                catalogo = productos,
                categoriaSeleccionada = categoriaSeleccionada,
                onCategoriaSelected = { productoViewModel.onCategoriaBusquedaChange(it) }
            )

            if (filtradoProductos.isNotEmpty()) {
                ProductSection(
                    title = "Productos Disponibles",
                    productos = filtradoProductos,
                    vm = productoViewModel,
                    onEdit = { producto ->
                        productoViewModel.cargarProdParaEditar(producto)
                        navController.navigate("formProducto")
                    }
                )
            } else {
                Text(
                    "No hay productos disponibles",
                    color = azulElectrico,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

//COMPOSE BANNER
@Composable
fun BannerCarousel() {
    Image(
        painter = painterResource(id = R.drawable.banner),
        contentDescription = "Banner Promocional",
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp)),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun Categoria(
    catalogo: List<ProductoEntity>,
    categoriaSeleccionada: String,
    onCategoriaSelected: (String) -> Unit
) {
    val categorias = catalogo.map { it.categoria }.distinct()

    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(8.dp)
    ) {
        //BOTON TODAS
        Button(
            onClick = { onCategoriaSelected("") },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (categoriaSeleccionada.isBlank()) verdeNeon else grisClaro
            ),
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Text("Todas", color = negroGrafito)
        }

        categorias.forEach { categoria ->
            Button(
                onClick = { onCategoriaSelected(categoria) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (categoria == categoriaSeleccionada) verdeNeon else grisClaro
                ),
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(categoria,color = negroGrafito)
            }
        }
    }
}