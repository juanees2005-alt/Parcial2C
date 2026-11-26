package com.example.parcial2.data

import com.example.parcial2.model.Expense
import com.example.parcial2.model.Trip
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RepositoriesTest {

    private val newTrip = Trip(
        name = "Cartagena",
        destination = "Colombia",
        startDate = "2026-12-01",
        endDate = "2026-12-05"
    )

    @Test
    fun categories_returnsFiveCategories() {
        assertEquals(5, InMemoryCategoryRepository().getCategories().size)
    }

    @Test
    fun saveTrip_generatesIdAndKeepsPreviousTrips() = runBlocking {
        val repository = InMemoryTripRepository()
        val before = repository.observeTrips().first().size

        repository.saveTrip(newTrip)
        repository.saveTrip(newTrip.copy(name = "Medellín"))

        val trips = repository.observeTrips().first()
        assertEquals(before + 2, trips.size)
        assertTrue(trips.last().id.isNotBlank())
        assertNotEquals(trips[trips.size - 2].id, trips.last().id)
    }

    @Test
    fun filterByTrip_returnsOnlyThatTripsExpenses() {
        val expenses = InMemoryExpenseRepository().filterByTrip("1")
        assertEquals(3, expenses.size)
        assertTrue(expenses.all { it.tripId == "1" })
    }

    @Test
    fun saveExpense_isAssociatedToItsTrip() = runBlocking {
        val repository = InMemoryExpenseRepository()
        repository.saveExpense(
            Expense(
                tripId = "2",
                description = "Taxi",
                amount = 15.0,
                categoryId = "3",
                categoryName = "Transporte",
                expenseDate = "2026-07-02"
            )
        )

        assertEquals(1, repository.filterByTrip("2").size)
        assertEquals(3, repository.filterByTrip("1").size)
    }
}
