package com.example.parcial2.viewmodel

import com.example.parcial2.data.InMemoryCategoryRepository
import com.example.parcial2.data.InMemoryExpenseRepository
import com.example.parcial2.data.InMemoryTripRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class ViewModelValidationTest {

    private fun registerViewModel(expenseRepository: InMemoryExpenseRepository) =
        RegisterExpenseViewModel(
            tripId = "1",
            tripRepository = InMemoryTripRepository(),
            expenseRepository = expenseRepository,
            categoryRepository = InMemoryCategoryRepository()
        )

    @Test
    fun createTrip_blankName_showsErrorAndDoesNotSave() = runBlocking {
        val repository = InMemoryTripRepository()
        val viewModel = CreateTripViewModel(repository)
        val before = repository.observeTrips().first().size
        var succeeded = false

        viewModel.saveTrip("", "Italia", "2026-06-10", "2026-06-20") { succeeded = true }

        assertFalse(succeeded)
        assertEquals("El nombre del viaje es obligatorio.", viewModel.errorMessage.value)
        assertEquals(before, repository.observeTrips().first().size)
    }

    @Test
    fun registerExpense_withoutCategory_showsErrorAndDoesNotSave() {
        val expenses = InMemoryExpenseRepository()
        val viewModel = registerViewModel(expenses)

        viewModel.saveExpense("Cena", "10", null, "2026-06-12") {}

        assertEquals("Debe seleccionar una categoría.", viewModel.errorMessage.value)
        assertEquals(3, expenses.filterByTrip("1").size)
    }

    @Test
    fun registerExpense_dateOutsideTrip_showsErrorAndDoesNotSave() {
        val expenses = InMemoryExpenseRepository()
        val viewModel = registerViewModel(expenses)
        val category = viewModel.categories.first()

        viewModel.saveExpense("Cena", "10", category, "2026-07-01") {}

        assertEquals(
            "La fecha del gasto no puede ser posterior al fin del viaje.",
            viewModel.errorMessage.value
        )
        assertEquals(3, expenses.filterByTrip("1").size)
    }
}