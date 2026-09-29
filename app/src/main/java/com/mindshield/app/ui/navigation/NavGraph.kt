package com.mindshield.app.ui.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mindshield.app.ui.coach.CoachChatScreen
import com.mindshield.app.ui.focus.FocusModeScreen
import com.mindshield.app.ui.home.HomeScreen
import com.mindshield.app.ui.insights.InsightsScreen
import com.mindshield.app.ui.onboarding.OnboardingScreen
import com.mindshield.app.ui.settings.SettingsScreen

@Composable
fun MindShieldNavHost(
    windowSizeClass: WindowSizeClass,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(onFinish = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Onboarding.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToCoach = { navController.navigate(Screen.Coach.route) },
                onNavigateToFocus = { navController.navigate(Screen.Focus.route) },
                onNavigateToInsights = { navController.navigate(Screen.Insights.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }
        composable(Screen.Coach.route) {
            CoachChatScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Screen.Insights.route) {
            InsightsScreen()
        }
        composable(Screen.Focus.route) {
            FocusModeScreen()
        }
        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}
