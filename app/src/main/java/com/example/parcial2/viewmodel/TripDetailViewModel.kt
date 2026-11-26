package com.example.parcial2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcial2.data.ExpenseRepository
import com.example.parcial2.data.TripRepository
import com.example.parcial2.domain.ExpenseCalculator
import com.example.parcial2.model.TripDetailUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TripDetailViewModel(
    private val tripId: String,
    tripRepository: TripRepository,
    expenseRepository: ExpenseRepository,
    private val expenseCalculator: ExpenseCalculator = ExpenseCalculator()
) : ViewModel() {

    private val trip = tripRepository.getTripById(tripId)

    val uiState: StateFlow<TripDetailUiState> = expenseRepository
        .observeExpensesByTrip(tripId)
        .map { tripExpenses ->
            TripDetailUiState(
                trip = trip,
                expenses = tripExpenses,
                totalExpenses = expenseCalculator.calculateTotal(tripExpenses, tripId)
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TripDetailUiState(trip = trip, isLoading = true)
        )
}