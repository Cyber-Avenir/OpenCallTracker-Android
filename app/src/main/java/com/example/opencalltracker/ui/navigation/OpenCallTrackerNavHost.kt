package com.example.opencalltracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.opencalltracker.ui.history.HistoryScreen
import com.example.opencalltracker.ui.main.MainScreen
import com.example.opencalltracker.ui.settings.SettingsScreen

@Composable
fun OpenCallTrackerNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    val navigateToHistory: () -> Unit = {
        navController.navigate(Route.HISTORY)
    }

    val navigateToSettings: () -> Unit = {
        navController.navigate(Route.SETTINGS)
    }

    val navigateBack: () -> Unit = {
        navController.popBackStack()
    }

    NavHost(navController = navController, startDestination = Route.MAIN, modifier = modifier) {
        composable(Route.MAIN) {
            MainScreen(onHistoryClick = navigateToHistory, onSettingsClick = navigateToSettings)
        }
        composable(Route.HISTORY) {
            HistoryScreen(onBackClick = navigateBack)
        }
        composable(Route.SETTINGS) {
            SettingsScreen(onBackClick = navigateBack)
        }
    }
}
