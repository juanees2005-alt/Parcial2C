package com.example.parcial2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.parcial2.data.CategoryRepository
import com.example.parcial2.data.ExpenseRepository
import com.example.parcial2.data.TripRepository
import com.example.parcial2.domain.ExpenseCalculator
import com.example.parcial2.domain.ExpenseValidator
import com.example.parcial2.domain.TripValidator
import com.example.parcial2.model.Category
import com.example.parcial2.model.Expense
import com.example.parcial2.model.Trip
import com.example.parcial2.ui.CreateTripScreen
import com.example.parcial2.ui.RegisterExpenseScreen
import com.example.parcial2.ui.TripDetailScreen
import com.example.parcial2.ui.TripListScreen
import com.example.parcial2.ui.theme.Parcial2Theme
import com.example.parcial2.viewmodel.CreateTripViewModel
import com.example.parcial2.viewmodel.RegisterExpenseViewModel
import com.example.parcial2.viewmodel.TripDetailViewModel
import com.example.parcial2.viewmodel.TripListViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

private sealed interface Screen {
    data object TripList : Screen
    data object CreateTrip : Screen
    data class TripDetail(val tripId: String) : Screen
    data class RegisterExpense(val tripId: String) : Screen
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tripRepository = object : TripRepository {
            private val tripsFlow = MutableStateFlow<List<Trip>>(emptyList())

            override fun observeTrips(): Flow<List<Trip>> = tripsFlow.asStateFlow()

            override fun getTripById(tripId: String): Trip? =
                tripsFlow.value.firstOrNull { it.id == tripId }

            override suspend fun saveTrip(trip: Trip) {
                val current = tripsFlow.value.toMutableList()
                val index = current.indexOfFirst { it.id == trip.id }
                if (index >= 0) {
                    current[index] = trip
                } else {
                    current.add(trip)
                }
                tripsFlow.value = current
            }
        }

        val expenseRepository = object : ExpenseRepository {
            private val expensesFlow = MutableStateFlow<List<Expense>>(emptyList())

            override fun observeExpensesByTrip(tripId: String): Flow<List<Expense>> =
                expensesFlow.map { list -> list.filter { it.tripId == tripId } }

            override fun filterByTrip(tripId: String): List<Expense> =
                expensesFlow.value.filter { it.tripId == tripId }

            override suspend fun saveExpense(expense: Expense) {
                val current = expensesFlow.value.toMutableList()
                val index = current.indexOfFirst { it.id == expense.id }
                if (index >= 0) {
                    current[index] = expense
                } else {
                    current.add(expense)
                }
                expensesFlow.value = current
            }
        }

        val categoryRepository = object : CategoryRepository {
            override fun getCategories(): List<Category> = emptyList()
        }

        val tripValidator = TripValidator()
        val expenseValidator = ExpenseValidator()
        val expenseCalculator = ExpenseCalculator()

        val tripListViewModel = TripListViewModel(tripRepository)
        val createTripViewModel = CreateTripViewModel(tripRepository, tripValidator)

        setContent {
            Parcial2Theme {
                var currentScreen by remember { mutableStateOf<Screen>(Screen.TripList) }

                when (val screen = currentScreen) {
                    Screen.TripList -> {
                        TripListScreen(
                            viewModel = tripListViewModel,
                            onCreateTripClick = { currentScreen = Screen.CreateTrip },
                            onTripClick = { tripId ->
                                currentScreen = Screen.TripDetail(tripId.toString())
                            }
                        )
                    }

                    Screen.CreateTrip -> {
                        CreateTripScreen(
                            viewModel = createTripViewModel,
                            onBack = { currentScreen = Screen.TripList }
                        )
                    }

                    is Screen.TripDetail -> {
                        val tripDetailViewModel = remember(screen.tripId) {
                            TripDetailViewModel(
                                tripId = screen.tripId,
                                tripRepository = tripRepository,
                                expenseRepository = expenseRepository,
                                expenseCalculator = expenseCalculator
                            )
                        }
                        TripDetailScreen(
                            viewModel = tripDetailViewModel,
                            onBack = { currentScreen = Screen.TripList },
                            onAddExpenseClick = {
                                currentScreen = Screen.RegisterExpense(screen.tripId)
                            }
                        )
                    }

                    is Screen.RegisterExpense -> {
                        val registerExpenseViewModel = remember(screen.tripId) {
                            RegisterExpenseViewModel(
                                tripId = screen.tripId,
                                tripRepository = tripRepository,
                                expenseRepository = expenseRepository,
                                categoryRepository = categoryRepository,
                                expenseValidator = expenseValidator
                            )
                        }
                        RegisterExpenseScreen(
                            viewModel = registerExpenseViewModel,
                            onBack = { currentScreen = Screen.TripDetail(screen.tripId) }
                        )
                    }
                }
            }
        }
    }
}