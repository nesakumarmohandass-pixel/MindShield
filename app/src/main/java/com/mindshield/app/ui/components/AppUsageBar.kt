package com.mindshield.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mindshield.app.ui.theme.SoftTeal

@Composable
fun AppUsageBar(
    appName: String,
    minutes: Int,
    maxMinutes: Int,
    modifier: Modifier = Modifier
) {
    val progress = if (maxMinutes > 0) (minutes.toFloat() / maxMinutes).coerceIn(0f, 1f) else 0f
    
    var launched by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        launched = true
    }
    
    val animatedProgress by animateFloatAsState(
        targetValue = if (launched) progress else 0f,
        animationSpec = tween(1000),
        label = "barProgress"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(appName, style = MaterialTheme.typography.bodyMedium)
                Text("${minutes / 60}h ${minutes % 60}m", style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .height(8.dp)
                        .background(SoftTeal, RoundedCornerShape(4.dp))
                )
            }
        }
    }
}
