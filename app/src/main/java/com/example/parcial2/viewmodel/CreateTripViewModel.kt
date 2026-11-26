package com.example.parcial2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcial2.data.TripRepository
import com.example.parcial2.domain.TripValidator
import com.example.parcial2.model.Trip
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreateTripViewModel(
    private val tripRepository: TripRepository,
    private val tripValidator: TripValidator = TripValidator()
) : ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun saveTrip(
        name: String,
        destination: String,
        startDate: String,
        endDate: String,
        onSuccess: () -> Unit
    ) {
        val trip = Trip(
            name = name.trim(),
            destination = destination.trim(),
            startDate = startDate.trim(),
            endDate = endDate.trim()
        )
        val validation = tripValidator.validateTrip(trip)
        if (!validation.isValid) {
            _errorMessage.value = validation.errorMessage
            return
        }

        _errorMessage.value = null
        viewModelScope.launch {
            tripRepository.saveTrip(trip)
            onSuccess()
        }
    }
}