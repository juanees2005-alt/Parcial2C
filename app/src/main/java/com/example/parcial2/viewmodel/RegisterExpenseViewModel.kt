package com.example.parcial2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcial2.data.CategoryRepository
import com.example.parcial2.data.ExpenseRepository
import com.example.parcial2.data.TripRepository
import com.example.parcial2.domain.ExpenseValidator
import com.example.parcial2.model.Category
import com.example.parcial2.model.Expense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterExpenseViewModel(
    private val tripId: String,
    private val tripRepository: TripRepository,
    private val expenseRepository: ExpenseRepository,
    categoryRepository: CategoryRepository,
    private val expenseValidator: ExpenseValidator = ExpenseValidator()
) : ViewModel() {

    val categories: List<Category> = categoryRepository.getCategories()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun saveExpense(
        description: String,
        amountText: String,
        selectedCategory: Category?,
        expenseDate: String,
        onSuccess: () -> Unit
    ) {
        val trip = tripRepository.getTripById(tripId)
        if (trip == null) {
            _errorMessage.value = "No se encontró el viaje asociado."
            return
        }

        val amount = amountText.replace(",", ".").toDoubleOrNull() ?: -1.0
        val expense = Expense(
            tripId = tripId,
            description = description.trim(),
            amount = amount,
            categoryId = selectedCategory?.id.orEmpty(),
            categoryName = selectedCategory?.name.orEmpty(),
            expenseDate = expenseDate.trim()
        )

        val validation = expenseValidator.validateExpense(expense, trip)
        if (!validation.isValid) {
            _errorMessage.value = validation.errorMessage
            return
        }

        _errorMessage.value = null
        viewModelScope.launch {
            expenseRepository.saveExpense(expense)
            onSuccess()
        }
    }
}