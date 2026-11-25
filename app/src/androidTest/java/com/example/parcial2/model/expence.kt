package com.example.parcial2.model

data class Expense(
    val id: String = "",
    val tripId: String,
    val description: String,
    val amount: Double,
    val categoryId: String,
    val categoryName: String = "",
    val expenseDate: String // Formato recomendado: YYYY-MM-DD
)