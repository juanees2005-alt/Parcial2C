package com.example.parcial2.domain

import com.example.parcial2.model.Expense
import com.example.parcial2.model.Trip
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExpenseValidatorTest {

    private val validator = ExpenseValidator()
    private val trip = Trip("1", "Europa 2026", "Italia", "2026-06-10", "2026-06-20")

    private fun validExpense() = Expense(
        tripId = "1",
        description = "Cena",
        amount = 28.50,
        categoryId = "1",
        categoryName = "Alimentación",
        expenseDate = "2026-06-12"
    )

    @Test
    fun validExpense_isValid() {
        assertTrue(validator.validateExpense(validExpense(), trip).isValid)
    }

    @Test
    fun blankDescription_isInvalid() {
        assertFalse(validator.validateExpense(validExpense().copy(description = ""), trip).isValid)
    }

    @Test
    fun zeroOrNegativeAmount_isInvalid() {
        val zero = validator.validateExpense(validExpense().copy(amount = 0.0), trip)
        val negative = validator.validateExpense(validExpense().copy(amount = -5.0), trip)
        assertEquals("El valor del gasto debe ser mayor a cero.", zero.errorMessage)
        assertFalse(negative.isValid)
    }

    @Test
    fun blankCategory_isInvalid() {
        assertFalse(validator.validateExpense(validExpense().copy(categoryId = ""), trip).isValid)
    }

    @Test
    fun blankDate_isInvalid() {
        assertFalse(validator.validateExpense(validExpense().copy(expenseDate = ""), trip).isValid)
    }

    @Test
    fun dateBeforeTripStart_isInvalid() {
        assertFalse(validator.validateExpense(validExpense().copy(expenseDate = "2026-06-09"), trip).isValid)
    }

    @Test
    fun dateAfterTripEnd_isInvalid() {
        assertFalse(validator.validateExpense(validExpense().copy(expenseDate = "2026-06-21"), trip).isValid)
    }
}