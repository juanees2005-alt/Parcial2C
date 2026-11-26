package com.example.parcial2.domain

import com.example.parcial2.model.Trip
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TripValidatorTest {

    private val validator = TripValidator()

    private fun validTrip() = Trip(
        name = "Europa 2026",
        destination = "Italia",
        startDate = "2026-06-10",
        endDate = "2026-06-20"
    )

    @Test
    fun validTrip_isValid() {
        assertTrue(validator.validateTrip(validTrip()).isValid)
    }

    @Test
    fun blankName_isInvalid() {
        val result = validator.validateTrip(validTrip().copy(name = " "))
        assertFalse(result.isValid)
        assertEquals("El nombre del viaje es obligatorio.", result.errorMessage)
    }

    @Test
    fun blankDestination_isInvalid() {
        assertFalse(validator.validateTrip(validTrip().copy(destination = "")).isValid)
    }

    @Test
    fun blankStartOrEndDate_isInvalid() {
        assertFalse(validator.validateTrip(validTrip().copy(startDate = "")).isValid)
        assertFalse(validator.validateTrip(validTrip().copy(endDate = "")).isValid)
    }

    @Test
    fun startAfterEnd_isInvalid() {
        val result = validator.validateTrip(validTrip().copy(startDate = "2026-06-21"))
        assertFalse(result.isValid)
    }
}