package com.example.parcial2.model

data class TripDetailUiState(
    val trip: Trip? = null,
    val expenses: List<Expense> = emptyList(),
    val totalExpenses: Double = 0.0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)