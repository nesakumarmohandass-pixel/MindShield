package com.mindshield.app.ui.focus

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.mindshield.app.ui.components.BreathingAnimation

@Composable
fun FocusModeScreen(
    viewModel: FocusViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BreathingAnimation()
        Button(onClick = {}) {
            Text("Start Focus")
        }
    }
}
