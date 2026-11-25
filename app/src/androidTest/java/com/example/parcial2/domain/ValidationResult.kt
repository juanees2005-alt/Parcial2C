package com.example.parcial2.domain

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)