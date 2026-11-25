package com.example.parcial2.data

import com.example.parcial2.model.Expense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.util.UUID

interface ExpenseRepository {
    fun observeExpensesByTrip(tripId: String): Flow<List<Expense>>
    fun filterByTrip(tripId: String): List<Expense>
    suspend fun saveExpense(expense: Expense)
}

class InMemoryExpenseRepository : ExpenseRepository {

    private val expensesFlow = MutableStateFlow(
        listOf(
            Expense("101", "1", "Cena en Trastevere", 28.50, "1", "Alimentación", "2026-06-12"),
            Expense("102", "1", "Hotel Roma", 260.00, "2", "Alojamiento", "2026-06-10"),
            Expense("103", "1", "Metro y bus", 7.00, "3", "Transporte", "2026-06-13")
        )
    )

    override fun observeExpensesByTrip(tripId: String): Flow<List<Expense>> {
        return expensesFlow.map { list -> list.filter { it.tripId == tripId } }
    }

    override fun filterByTrip(tripId: String): List<Expense> {
        return expensesFlow.value.filter { it.tripId == tripId }
    }

    override suspend fun saveExpense(expense: Expense) {
        val newExpense = if (expense.id.isBlank()) {
            expense.copy(id = UUID.randomUUID().toString())
        } else {
            expense
        }
        expensesFlow.update { current -> current + newExpense }
    }
}