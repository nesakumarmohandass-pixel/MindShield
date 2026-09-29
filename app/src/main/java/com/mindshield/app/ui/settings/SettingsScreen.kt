package com.mindshield.app.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SettingsScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Settings")
        Slider(value = 4f, onValueChange = {}, valueRange = 1f..8f)
    }
}
