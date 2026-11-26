package com.example.parcial2.model

data class Trip(
    val id: String = "",
    val name: String,
    val destination: String,
    val startDate: String, // Formato recomendado: YYYY-MM-DD
    val endDate: String    // Formato recomendado: YYYY-MM-DD
)