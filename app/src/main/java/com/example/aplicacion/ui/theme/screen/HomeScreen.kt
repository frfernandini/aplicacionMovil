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
    //LISTADO DINAMICO DE PRODUCTOS
    //ACTIVAR SI SE DESEA IMPLEMENTAR EL FORMULARIO
    //val productos by productoViewModel.productos.collectAsState()

    //LISTADO ESTATICO DE PRODUCTOS
    val productos by productoViewModel.catalogo.collectAsState()
    val categoriaSeleccionada by productoViewModel.busquedaCategoria.collectAsState()

    //ACA SE FILTRAN LOS PRODUCTOS SEGUN LA CATEGORIA SELECCIONADA
    val productosFiltrados =
        if (categoriaSeleccionada.isEmpty())
            productos
        else
            productos.filter { it.categoria == categoriaSeleccionada }
    Scaffold(
        topBar = { TopBar(navController = navController) },
        bottomBar = { BottomNavBar() },
        //FORMULARIO DEL PRODUCTO
        /*floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("formProducto") },
                containerColor = AccentGreen
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Producto")
            }
        },*/
        containerColor = DarkBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            BannerCarousel()

            if (productos.isNotEmpty()) {
                ProductSection(
                    title = "Productos Disponibles",
                    productos = productos,
                    vm = productoViewModel,
                    onEdit = { producto ->
                        productoViewModel.cargarProdParaEditar(producto) //<- Nos Sirve para llenar el formulario
                                                                        // con la informacion correspondiente del producto
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

//COMPOSE PARA FILTRAR POR CATEGORIA
/*@Composable
fun CategoriaBar(productoViewModel: ProductoViewModel) {
    val productos by productoViewModel.productos.collectAsState()
    val categoriaSeleccionada by productoViewModel.busquedaCategoria.collectAsState()

    val categorias = productos.map { it.categoria }.distinct()

    // Estado para abrir/cerrar el dropdown
    var expandir by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = { expandir = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentGreen,
                contentColor = TextColor
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = if (categoriaSeleccionada.isEmpty()) "Categorías" else categoriaSeleccionada
            )
        }

        DropdownMenu(
            expanded = expandir,
            onDismissRequest = { expandir = false }
        ) {
            // Opción "Todas"
            DropdownMenuItem(onClick = {
                productoViewModel.onCategoriaBusquedaChange("")
                expandir = false
            }) {
                Text("Todas")
            }

            // Opciones de las categorías
            categorias.forEach { categoria ->
                DropdownMenuItem(onClick = {
                    productoViewModel.onCategoriaBusquedaChange(categoria)
                    expandir = false
                }) {
                    Text(categoria)
                }
            }
        }
    }
}*/