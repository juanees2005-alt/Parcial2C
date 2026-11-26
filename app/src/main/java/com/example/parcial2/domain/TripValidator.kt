package com.example.parcial2.domain

import com.example.parcial2.model.Trip

class TripValidator {

    fun validateTrip(trip: Trip): ValidationResult {
        if (trip.name.isBlank()) {
            return ValidationResult(false, "El nombre del viaje es obligatorio.")
        }
        if (trip.destination.isBlank()) {
            return ValidationResult(false, "El destino del viaje es obligatorio.")
        }
        if (trip.startDate.isBlank()) {
            return ValidationResult(false, "La fecha de inicio es obligatoria.")
        }
        if (trip.endDate.isBlank()) {
            return ValidationResult(false, "La fecha de finalización es obligatoria.")
        }
        if (trip.startDate > trip.endDate) {
            return ValidationResult(false, "La fecha de inicio no puede ser posterior a la fecha de finalización.")
        }
        return ValidationResult(true)
    }
}