package com.example.parcial2.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.parcial2.model.Expense
import com.example.parcial2.viewmodel.TripDetailViewModel
import kotlin.text.format
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailScreen(
    viewModel: TripDetailViewModel,
    onBack: () -> Unit,
    onAddExpenseClick: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val trip = state.trip

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(trip?.name ?: "Detalle del viaje") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            trip?.let {
                FloatingActionButton(onClick = { onAddExpenseClick(it.id) }) {
                    Icon(Icons.Default.Add, contentDescription = "Registrar gasto")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            trip?.let {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = it.name, style = MaterialTheme.typography.titleLarge)
                        Text(text = "Destino: ${it.destination}")
                        Text(text = "Período: ${it.startDate} - ${it.endDate}")
                    }
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Total de gastos", style = MaterialTheme.typography.labelLarge)
                    Text(
                        text = "$ ${"%.2f".format(state.totalExpenses)}",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }

            Text(
                text = "Gastos registrados (${state.expenses.size})",
                style = MaterialTheme.typography.titleMedium
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.expenses, key = { it.id }) { expense ->
                    ExpenseItem(expense = expense)
                }
            }
        }
    }
}

@Composable
fun ExpenseItem(expense: Expense) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = expense.categoryName, style = MaterialTheme.typography.titleSmall)
                Text(text = expense.description, style = MaterialTheme.typography.bodyMedium)
                Text(text = expense.expenseDate, style = MaterialTheme.typography.bodySmall)
            }
            Text(
                text = "$ ${"%.2f".format(expense.amount)}",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}