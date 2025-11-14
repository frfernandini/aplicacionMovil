package com.example.aplicacion.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aplicacion.data.remote.dto.ProductoDto
import com.example.aplicacion.ui.components.FormularioTextField
import com.example.aplicacion.ui.theme.azulElectrico
import com.example.aplicacion.ui.theme.negroGrafito
import com.example.aplicacion.viewmodel.CheckoutViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    checkoutViewModel: CheckoutViewModel,
    carrito: List<ProductoDto>,
    total: Double,
    onPedidoExitoso: () -> Unit
) {
    val uiState by checkoutViewModel.uiState.collectAsState()

    LaunchedEffect(uiState.pedidoExitoso) {
        if (uiState.pedidoExitoso) {
            onPedidoExitoso()
            checkoutViewModel.onNavegacionRealizada()
        }
    }

    Scaffold(
        containerColor = negroGrafito,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = negroGrafito),
                title = { Text("Finalizar Compra", color = azulElectrico) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = azulElectrico)
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(containerColor = Color.DarkGray) {
                Button(
                    onClick = {
                        checkoutViewModel.crearPedido(carrito)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(color = Color.White)
                    } else {
                        Text("Confirmar Pedido", fontSize = 18.sp)
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Resumen del pedido
            item {
                Text("Resumen de tu compra", style = MaterialTheme.typography.titleLarge, color = azulElectrico)
                Spacer(modifier = Modifier.height(8.dp))
                carrito.forEach {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${it.cantidad}x ${it.nombre}", color = Color.White)
                        Text("$%.2f".format((it.precio ?: 0.0) * it.cantidad), color = Color.White)
                    }
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp), color = azulElectrico)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total", color = azulElectrico, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("$%.2f".format(total), color = azulElectrico, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            }

            // Formulario de envío
            item {
                Text("Datos de Envío", style = MaterialTheme.typography.titleLarge, color = azulElectrico)
                Spacer(modifier = Modifier.height(8.dp))
                FormularioTextField(
                    value = uiState.direccion,
                    onValueChange = checkoutViewModel::onDireccionChange,
                    label = "Dirección de Envío",
                    isError = uiState.errorDireccion != null
                )
                uiState.errorDireccion?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                Spacer(modifier = Modifier.height(8.dp))

                FormularioTextField(
                    value = uiState.notas,
                    onValueChange = checkoutViewModel::onNotasChange,
                    label = "Notas adicionales (opcional)"
                )
            }

            // Mensaje de error general
            if (uiState.errorGeneral != null) {
                item {
                    Text(uiState.errorGeneral!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
                }
            }
        }
    }
}
