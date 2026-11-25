package com.example.parcial2.domain

import com.example.parcial2.model.Expense
import com.example.parcial2.model.Trip

class ExpenseValidator {

    fun validateAmount(amount: Double): ValidationResult {
        return if (amount <= 0.0) {
            ValidationResult(false, "El valor del gasto debe ser mayor a cero.")
        } else {
            ValidationResult(true)
        }
    }

    fun validateExpense(expense: Expense, trip: Trip): ValidationResult {
        if (expense.description.isBlank()) {
            return ValidationResult(false, "La descripción del gasto es obligatoria.")
        }
        val amountCheck = validateAmount(expense.amount)
        if (!amountCheck.isValid) {
            return amountCheck
        }
        if (expense.categoryId.isBlank()) {
            return ValidationResult(false, "Debe seleccionar una categoría.")
        }
        if (expense.expenseDate.isBlank()) {
            return ValidationResult(false, "La fecha del gasto es obligatoria.")
        }
        if (expense.expenseDate < trip.startDate) {
            return ValidationResult(false, "La fecha del gasto no puede ser anterior al inicio del viaje.")
        }
        if (expense.expenseDate > trip.endDate) {
            return ValidationResult(false, "La fecha del gasto no puede ser posterior al fin del viaje.")
        }
        return ValidationResult(true)
    }
}