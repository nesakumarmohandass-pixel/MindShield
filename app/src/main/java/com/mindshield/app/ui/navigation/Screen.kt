package com.mindshield.app.ui.navigation

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object Coach : Screen("coach")
    object Insights : Screen("insights")
    object Focus : Screen("focus")
    object Settings : Screen("settings")
}
