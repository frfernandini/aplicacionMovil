package com.example.aplicacion.ui.screen


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplicacion.R
import com.example.aplicacion.model.local.ProductoEntity
import com.example.aplicacion.ui.components.ProductSection
import com.example.aplicacion.ui.components.TopBar
import com.example.aplicacion.ui.theme.azulElectrico
import com.example.aplicacion.ui.components.BottomNavBar
import com.example.aplicacion.ui.components.Loader
import com.example.aplicacion.ui.theme.grisClaro
import com.example.aplicacion.ui.theme.negroGrafito
import com.example.aplicacion.ui.theme.verdeNeon
import com.example.aplicacion.ui.theme.verdeOscuro
import com.example.aplicacion.viewmodel.ProductoViewModel
import kotlinx.coroutines.delay


val DarkBackground = Color(0xFF1f1f1f)
val CardBackground = Color(0xFF1E1E1E)
val AccentGreen = Color(0xFF39FF14)
val TextColor = Color.White


@Composable
fun HomeScreen(
    navController: NavController,
    productoViewModel: ProductoViewModel
) {
    val productos by productoViewModel.catalogo.collectAsState()
    val categoriaSeleccionada by productoViewModel.busquedaCategoria.collectAsState()

    var isLoading by remember { mutableStateOf(true) }

    // Simula que carga al entrar
    LaunchedEffect(Unit) {
        delay(1000)
        isLoading = false
    }

    val filtradoProductos =
        if (categoriaSeleccionada.isEmpty())
            productos
        else
            productos.filter { it.categoria == categoriaSeleccionada }


    Scaffold(
        topBar = { TopBar(navController = navController) },
        bottomBar = { BottomNavBar(navController = navController) },
        containerColor = DarkBackground
    ) { innerPadding ->
        if (isLoading){
            Loader()
        }else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                BannerCarousel()
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
}


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
        //BOTON MOSTRAR TODO
        val btColorTodas by animateColorAsState(
            targetValue = if (categoriaSeleccionada.isBlank()) verdeOscuro else grisClaro,
            animationSpec = tween(durationMillis = 400)
        )
        val textColorTodas by animateColorAsState(
            targetValue = if (categoriaSeleccionada.isBlank()) Color.White else negroGrafito,
            animationSpec = tween(durationMillis = 400)
        )

        Button(
            onClick = { onCategoriaSelected("") },
            colors = ButtonDefaults.buttonColors(containerColor = btColorTodas),
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Text("Todas", color = textColorTodas)
        }

        //BOTONES CATEGORIAS
        categorias.forEach { categoria ->
            val btColor by animateColorAsState(
                targetValue = if (categoria == categoriaSeleccionada) verdeOscuro else grisClaro,
                animationSpec = tween(durationMillis = 400)
            )
            val textColor by animateColorAsState(
                targetValue = if (categoria == categoriaSeleccionada) Color.White else negroGrafito,
                animationSpec = tween(durationMillis = 400)
            )

            Button(
                onClick = { onCategoriaSelected(categoria) },
                colors = ButtonDefaults.buttonColors(containerColor = btColor),
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(categoria, color = textColor)
            }
        }
    }
}

