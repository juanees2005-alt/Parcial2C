package com.example.parcial2.domain

import com.example.parcial2.model.Expense

class ExpenseCalculator {

    fun calculateTotal(expenses: List<Expense>, tripId: String): Double {
        return expenses
            .filter { it.tripId == tripId }
            .sumOf { it.amount }
    }
}