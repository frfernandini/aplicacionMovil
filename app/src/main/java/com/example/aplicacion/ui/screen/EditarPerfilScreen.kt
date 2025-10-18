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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.aplicacion.ui.components.FormularioTextField // ¡Reutilizando tu componente!
import com.example.aplicacion.ui.theme.verdeNeon
import com.example.aplicacion.viewmodel.EditarPerfilViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPerfilScreen(navController: NavController,viewModel: EditarPerfilViewModel) {

    // --- ESTADOS TEMPORALES PARA LA UI ---
    // En el futuro, estos vendrán de un ViewModel (EditProfileViewModel)
    val editarPerfilState by viewModel.editarPerfilState.collectAsState()

    // --- LÓGICA PARA SELECCIONAR IMAGEN ---
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
            navController.popBackStack() // Vuelve a la pantalla de perfil
            viewModel.onNavegacionRealizada() // Resetea el estado para no volver a navegar
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil", color = Color.White) },
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
                    containerColor = Color(0xFF121212) // Mismo fondo que el resto de la pantalla
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // --- SECCIÓN DE IMAGEN DE PERFIL ---
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(2.dp, verdeNeon, CircleShape)
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
                        tint = Color.Gray, // Un color que se vea bien en el fondo
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.DarkGray.copy(alpha = 0.5f))
                            .padding(24.dp)
                    )
                }


                Icon(
                    imageVector = Icons.Default.AddAPhoto,
                    contentDescription = "Cambiar foto",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            FormularioTextField(
                value = editarPerfilState.nombre,
                onValueChange = { viewModel.onNombreChange(it) },
                label = "Nombre de Usuario"
            )

            Spacer(modifier = Modifier.height(16.dp))

            FormularioTextField(
                value = editarPerfilState.email,
                onValueChange = {}, // No se puede cambiar
                label = "Correo Electrónico",
            )

            Spacer(modifier = Modifier.height(16.dp))

            FormularioTextField(
                value = editarPerfilState.contrasena,
                onValueChange = { viewModel.onContrasenaChange(it) },
                label = "Contraseña",
                keyboardType = KeyboardType.Password,
                isPasswordToggle = true
            )
        }
    }
}