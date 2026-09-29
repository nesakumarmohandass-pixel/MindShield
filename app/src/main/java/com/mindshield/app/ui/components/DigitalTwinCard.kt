package com.mindshield.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindshield.app.ui.theme.*

enum class TwinState {
    Thriving, Happy, Neutral, Tired, Stressed
}

@Composable
fun DigitalTwinCard(
    state: TwinState,
    level: Int,
    streak: Int,
    healthProgress: Float,
    modifier: Modifier = Modifier
) {
    val (emoji, color1, color2, label) = when (state) {
        TwinState.Thriving -> listOf("🌱", TwinThriving, SoftTeal, "Thriving Environment")
        TwinState.Happy -> listOf("😊", TwinHappy, SoftTeal, "Happy Environment")
        TwinState.Neutral -> listOf("😐", TwinNeutral, Color.Gray, "Neutral Environment")
        TwinState.Tired -> listOf("😴", TwinTired, WarmCoral, "Tired Environment")
        TwinState.Stressed -> listOf("😰", TwinStressed, WarmCoral, "Stressed Environment")
    }
    
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "emoji_scale"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf((color1 as Color).copy(alpha = 0.4f), (color2 as Color).copy(alpha = 0.1f))
                    )
                )
                .padding(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Lvl $level", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Text("🔥 $streak", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = emoji as String,
                    fontSize = 72.sp,
                    modifier = Modifier.scale(scale)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(text = label as String, style = MaterialTheme.typography.titleMedium)
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                ) {
                    val animatedHealth by animateFloatAsState(targetValue = healthProgress, tween(1000), label="health")
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(animatedHealth)
                            .height(8.dp)
                            .background(color1, RoundedCornerShape(4.dp))
                    )
                }
            }
        }
    }
}
