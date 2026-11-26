package com.example.parcial2.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.parcial2.viewmodel.CreateTripViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTripScreen(
    viewModel: CreateTripViewModel,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    val errorMessage by viewModel.errorMessage.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear viaje") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre del viaje *") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = destination,
                onValueChange = { destination = it },
                label = { Text("Destino *") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = startDate,
                onValueChange = { startDate = it },
                label = { Text("Fecha de inicio (YYYY-MM-DD) *") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = endDate,
                onValueChange = { endDate = it },
                label = { Text("Fecha de fin (YYYY-MM-DD) *") },
                modifier = Modifier.fillMaxWidth()
            )

            errorMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = {
                    viewModel.saveTrip(name, destination, startDate, endDate, onSuccess = onBack)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }
        }
    }
}