package com.mindshield.app.ui.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MindShieldNavHost(
    windowSizeClass: WindowSizeClass,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Onboarding.route
) {
    // In a real app, check DataStore for onboarding status
    var isFirstLaunch by remember { mutableStateOf(true) }

    NavHost(
        navController = navController,
        startDestination = if (isFirstLaunch) Screen.Onboarding.route else Screen.Home.route
    ) {
        composable(Screen.Onboarding.route) {
            // OnboardingScreen(navController = navController)
        }
        composable(Screen.Home.route) {
            // HomeScreen(navController = navController, windowSizeClass = windowSizeClass)
        }
        composable(Screen.Coach.route) {
            // CoachScreen(navController = navController)
        }
        composable(Screen.Insights.route) {
            // InsightsScreen(navController = navController)
        }
        composable(Screen.Focus.route) {
            // FocusScreen(navController = navController)
        }
        composable(Screen.Settings.route) {
            // SettingsScreen(navController = navController)
        }
    }
}
