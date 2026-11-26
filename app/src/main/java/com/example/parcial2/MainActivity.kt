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
import com.example.parcial2.ui.CreateTripScreen
import com.example.parcial2.ui.RegisterExpenseScreen
import com.example.parcial2.ui.TripDetailScreen
import com.example.parcial2.ui.TripListScreen
import com.example.parcial2.ui.theme.Parcial2CTheme // Ajusta el nombre del Theme si en ui/theme se llama distinto
import com.example.parcial2.viewmodel.CreateTripViewModel
import com.example.parcial2.viewmodel.RegisterExpenseViewModel
import com.example.parcial2.viewmodel.TripDetailViewModel
import com.example.parcial2.viewmodel.TripListViewModel

private sealed interface Screen {
    data object TripList : Screen
    data object CreateTrip : Screen
    data class TripDetail(val tripId: Int) : Screen
    data class RegisterExpense(val tripId: Int) : Screen
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tripRepository = TripRepository()
        val expenseRepository = ExpenseRepository()
        val categoryRepository = CategoryRepository()

        val tripValidator = TripValidator()
        val expenseValidator = ExpenseValidator()
        val expenseCalculator = ExpenseCalculator()

        val tripListViewModel = TripListViewModel(tripRepository)
        val createTripViewModel = CreateTripViewModel(tripRepository, tripValidator)
        val tripDetailViewModel = TripDetailViewModel(
            tripRepository = tripRepository,
            expenseRepository = expenseRepository,
            expenseCalculator = expenseCalculator
        )
        val registerExpenseViewModel = RegisterExpenseViewModel(
            tripRepository = tripRepository,
            expenseRepository = expenseRepository,
            categoryRepository = categoryRepository,
            expenseValidator = expenseValidator
        )

        setContent {
            Parcial2CTheme {
                var currentScreen by remember { mutableStateOf<Screen>(Screen.TripList) }

                when (val screen = currentScreen) {
                    Screen.TripList -> {
                        TripListScreen(
                            viewModel = tripListViewModel,
                            onAddTripClick = { currentScreen = Screen.CreateTrip },
                            onTripClick = { tripId -> currentScreen = Screen.TripDetail(tripId) }
                        )
                    }
                    Screen.CreateTrip -> {
                        CreateTripScreen(
                            viewModel = createTripViewModel,
                            onBack = { currentScreen = Screen.TripList }
                        )
                    }
                    is Screen.TripDetail -> {
                        TripDetailScreen(
                            tripId = screen.tripId,
                            viewModel = tripDetailViewModel,
                            onBack = { currentScreen = Screen.TripList },
                            onAddExpenseClick = { currentScreen = Screen.RegisterExpense(screen.tripId) }
                        )
                    }
                    is Screen.RegisterExpense -> {
                        RegisterExpenseScreen(
                            tripId = screen.tripId,
                            viewModel = registerExpenseViewModel,
                            onBack = { currentScreen = Screen.TripDetail(screen.tripId) }
                        )
                    }
                }
            }
        }
    }
}