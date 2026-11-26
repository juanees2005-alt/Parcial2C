package com.example.parcial2.domain

import com.example.parcial2.model.Expense
import org.junit.Assert.assertEquals
import org.junit.Test

class ExpenseCalculatorTest {

    private val calculator = ExpenseCalculator()

    private fun expense(tripId: String, amount: Double) = Expense(
        tripId = tripId,
        description = "Gasto",
        amount = amount,
        categoryId = "1",
        expenseDate = "2026-06-12"
    )

    @Test
    fun sumsExpensesOfTheTrip() {
        val expenses = listOf(expense("1", 28.50), expense("1", 260.0), expense("1", 7.0))
        assertEquals(295.50, calculator.calculateTotal(expenses, "1"), 0.001)
    }

    @Test
    fun ignoresExpensesFromOtherTrips() {
        val expenses = listOf(expense("1", 100.0), expense("2", 999.0))
        assertEquals(100.0, calculator.calculateTotal(expenses, "1"), 0.001)
    }

    @Test
    fun noExpenses_totalIsZero() {
        assertEquals(0.0, calculator.calculateTotal(emptyList(), "1"), 0.001)
    }
}
