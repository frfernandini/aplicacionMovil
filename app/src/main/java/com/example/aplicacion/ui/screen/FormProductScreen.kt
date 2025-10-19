package com.example.aplicacion.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.aplicacion.ui.theme.*
import com.example.aplicacion.viewmodel.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormProductScreen(
    vm: ProductoViewModel,
    onBack: () -> Unit,
    onSaved: () -> Unit,
    onDelete: () -> Unit
) {
    val prod by vm.estado.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (prod.id == null)
                    "Agregar Nuevo Producto"
                else
                    "Editar Producto #${prod.id}") }
            )
        },
        containerColor = DarkBackground
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            //NOMBRE
            OutlinedTextField(
                value = prod.nombre,
                onValueChange = vm::onNombreCharge,
                label = { Text("Nombre del Producto") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            //MANEJAR ERROR DE FORMA INDIVIDUAL(POR CAMPO)
            prod.errores.nombre?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
            //DESCRIPCION
            OutlinedTextField(
                value = prod.descripcion,
                onValueChange = vm::onDescripcionCharge,
                label = { Text("Descripcion del Producto") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            //MANEJAR ERROR DE FORMA INDIVIDUAL(POR CAMPO)
            prod.errores.descripcion?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
            //PRECIO
            OutlinedTextField(
                value = prod.precio,
                onValueChange = vm::onPrecioCharge,
                label = { Text("Monto") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            //MANEJAR ERROR DE FORMA INDIVIDUAL(POR CAMPO)
            prod.errores.precio?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
            //CATEGORIA
            OutlinedTextField(
                value = prod.categoria,
                onValueChange = vm::onCategoriaCharge,
                label = { Text("Categoría") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            //MANEJAR ERROR DE FORMA INDIVIDUAL(POR CAMPO)
            prod.errores.categoria?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onSaved,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = verdeNeon,
                        contentColor = negroGrafito))
                { Text("Guardar") }
                OutlinedButton(onClick = onBack,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = blanco,
                        contentColor = azulElectrico))
                { Text("Cancelar") }
                if (prod.id != null && prod.id != 0){
                Button(onClick = onDelete,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF3B30),
                        contentColor = blanco))
                { Text("Eliminar")}}
            }

        }
    }
}