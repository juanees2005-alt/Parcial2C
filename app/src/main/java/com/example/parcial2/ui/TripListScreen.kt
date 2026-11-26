package com.example.parcial2.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.parcial2.model.Trip
import com.example.parcial2.viewmodel.TripListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripListScreen(
    viewModel: TripListViewModel,
    onTripClick: (String) -> Unit,
    onCreateTripClick: () -> Unit
) {
    val trips by viewModel.trips.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mis viajes") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateTripClick) {
                Icon(Icons.Default.Add, contentDescription = "Crear viaje")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(trips, key = { it.id }) { trip ->
                TripItem(trip = trip, onClick = { onTripClick(trip.id) })
            }
        }
    }
}

@Composable
fun TripItem(trip: Trip, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = trip.name, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = trip.destination, style = MaterialTheme.typography.bodyMedium)
        }
    }
}