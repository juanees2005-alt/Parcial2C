package com.example.parcial2.viewmodel

import com.example.parcial2.data.InMemoryCategoryRepository
import com.example.parcial2.data.InMemoryExpenseRepository
import com.example.parcial2.data.InMemoryTripRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class ViewModelValidationTest {

    @Test
    fun createTrip_blankName_showsErrorAndDoesNotSucceed() {
        val viewModel = CreateTripViewModel(InMemoryTripRepository())
        var succeeded = false

        viewModel.saveTrip("", "Italia", "2026-06-10", "2026-06-20") { succeeded = true }

        assertFalse(succeeded)
        assertEquals("El nombre del viaje es obligatorio.", viewModel.errorMessage.value)
    }

    @Test
    fun registerExpense_withoutCategory_showsError() {
        val viewModel = RegisterExpenseViewModel(
            tripId = "1",
            tripRepository = InMemoryTripRepository(),
            expenseRepository = InMemoryExpenseRepository(),
            categoryRepository = InMemoryCategoryRepository()
        )

        viewModel.saveExpense("Cena", "10", null, "2026-06-12") {}

        assertEquals("Debe seleccionar una categoría.", viewModel.errorMessage.value)
    }

    @Test
    fun registerExpense_dateOutsideTrip_showsError() {
        val viewModel = RegisterExpenseViewModel(
            tripId = "1",
            tripRepository = InMemoryTripRepository(),
            expenseRepository = InMemoryExpenseRepository(),
            categoryRepository = InMemoryCategoryRepository()
        )
        val category = viewModel.categories.first()

        viewModel.saveExpense("Cena", "10", category, "2026-07-01") {}

        assertEquals(
            "La fecha del gasto no puede ser posterior al fin del viaje.",
            viewModel.errorMessage.value
        )
    }
}
