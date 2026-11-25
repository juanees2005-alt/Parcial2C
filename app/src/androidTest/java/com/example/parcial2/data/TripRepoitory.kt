package com.example.parcial2.data

import com.example.parcial2.model.Trip
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

interface TripRepository {
    fun observeTrips(): Flow<List<Trip>>
    fun getTripById(tripId: String): Trip?
    suspend fun saveTrip(trip: Trip)
}

class InMemoryTripRepository : TripRepository {

    private val tripsFlow = MutableStateFlow(
        listOf(
            Trip("1", "Europa 2026", "Italia", "2026-06-10", "2026-06-20"),
            Trip("2", "Santa Marta", "Colombia", "2026-07-01", "2026-07-08")
        )
    )

    override fun observeTrips(): Flow<List<Trip>> = tripsFlow.asStateFlow()

    override fun getTripById(tripId: String): Trip? {
        return tripsFlow.value.find { it.id == tripId }
    }

    override suspend fun saveTrip(trip: Trip) {
        val newTrip = if (trip.id.isBlank()) {
            trip.copy(id = UUID.randomUUID().toString())
        } else {
            trip
        }
        tripsFlow.update { current -> current + newTrip }
    }
}