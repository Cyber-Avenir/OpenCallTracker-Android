package com.example.opencalltracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.opencalltracker.ui.history.HistoryScreen
import com.example.opencalltracker.ui.main.MainScreen

@Composable
fun OpenCallTrackerNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    val navigateToHistory: () -> Unit = {
        navController.navigate(Route.HISTORY)
    }

    val navigateBack: () -> Unit = {
        navController.popBackStack()
    }

    NavHost(navController = navController, startDestination = Route.MAIN, modifier = modifier) {
        composable(Route.MAIN) {
            MainScreen(onHistoryClick = navigateToHistory)
        }
        composable(Route.HISTORY) {
            HistoryScreen(onBackClick = navigateBack)
        }
    }
}
