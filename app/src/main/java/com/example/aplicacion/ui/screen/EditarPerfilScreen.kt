package com.example.aplicacion.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.aplicacion.ui.theme.verdeNeon
import com.example.aplicacion.viewmodel.EditarPerfilViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPerfilScreen(navController: NavController,viewModel: EditarPerfilViewModel) {

    val editarPerfilState by viewModel.editarPerfilState.collectAsState()

    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                viewModel.onImagenSeleccionada(uri)
            }
        }
    )
    LaunchedEffect(key1 = editarPerfilState.guardadoExitoso) {
        if (editarPerfilState.guardadoExitoso) {
            navController.popBackStack()
            viewModel.onNavegacionRealizada()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Foto de Perfil", color = Color.White) }, // Título actualizado
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    Button(
                        onClick = { viewModel.guardarCambios() },
                        colors = ButtonDefaults.buttonColors(containerColor = verdeNeon)
                    ) {
                        Text("Guardar", color = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF121212)
                )
            )
        },
        containerColor = Color(0xFF121212)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Centramos el contenido
        ) {

            // --- SECCIÓN DE IMAGEN DE PERFIL ---
            Box(
                modifier = Modifier
                    .size(200.dp) // Hacemos la imagen más grande
                    .clip(CircleShape)
                    .border(3.dp, verdeNeon, CircleShape)
                    .clickable {
                        pickMediaLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
            ) {
                val imagenUri = editarPerfilState.imagenUri
                if(imagenUri.isNotEmpty()){
                    Image(
                        painter = rememberAsyncImagePainter(model = imagenUri.toUri()),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }else {
                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = "Añadir foto de perfil",
                        tint = Color.Gray,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.DarkGray.copy(alpha = 0.5f))
                            .padding(48.dp) // Aumentamos el padding del ícono
                    )
                }


                Icon(
                    imageVector = Icons.Default.AddAPhoto,
                    contentDescription = "Cambiar foto",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        .padding(12.dp) // Hacemos el ícono de la cámara más grande
                )
            }
            // Se eliminaron los campos de texto para nombre, email y contraseña
        }
    }
}
