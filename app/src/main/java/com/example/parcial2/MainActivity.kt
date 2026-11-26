package com.example.parcial2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.parcial2.data.InMemoryCategoryRepository
import com.example.parcial2.data.InMemoryExpenseRepository
import com.example.parcial2.data.InMemoryTripRepository
import com.example.parcial2.domain.ExpenseCalculator
import com.example.parcial2.domain.ExpenseValidator
import com.example.parcial2.domain.TripValidator
import com.example.parcial2.ui.CreateTripScreen
import com.example.parcial2.ui.RegisterExpenseScreen
import com.example.parcial2.ui.TripDetailScreen
import com.example.parcial2.ui.TripListScreen
import com.example.parcial2.ui.theme.Parcial2Theme
import com.example.parcial2.viewmodel.CreateTripViewModel
import com.example.parcial2.viewmodel.RegisterExpenseViewModel
import com.example.parcial2.viewmodel.TripDetailViewModel
import com.example.parcial2.viewmodel.TripListViewModel

private sealed interface Screen {
    data object TripList : Screen
    data object CreateTrip : Screen
    data class TripDetail(val tripId: String) : Screen
    data class RegisterExpense(val tripId: String) : Screen
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tripRepository = InMemoryTripRepository()
        val expenseRepository = InMemoryExpenseRepository()
        val categoryRepository = InMemoryCategoryRepository()

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
                                currentScreen = Screen.TripDetail(tripId)
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