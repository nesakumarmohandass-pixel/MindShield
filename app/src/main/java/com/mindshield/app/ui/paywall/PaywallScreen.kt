package com.mindshield.app.ui.paywall

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PaywallScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Upgrade to Premium")
        Button(onClick = {}) { Text("Start 7-Day Free Trial") }
    }
}
