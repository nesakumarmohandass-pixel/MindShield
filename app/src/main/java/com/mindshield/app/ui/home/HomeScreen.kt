package com.mindshield.app.ui.home

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mindshield.app.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToCoach: () -> Unit,
    onNavigateToFocus: () -> Unit,
    onNavigateToInsights: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToCoach,
                icon = { Icon(Icons.Default.Chat, contentDescription = null) },
                text = { Text("Talk to Coach") },
                containerColor = MaterialTheme.colorScheme.primary
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                // Greeting
                item {
                    Text(
                        text = "${uiState.greeting} \uD83D\uDC4B",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Here's your digital wellness summary",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Digital Twin Card
                item {
                    DigitalTwinCard(
                        state = when (uiState.twinState.mood) {
                            com.mindshield.app.domain.model.TwinMood.THRIVING -> TwinState.Thriving
                            com.mindshield.app.domain.model.TwinMood.HAPPY -> TwinState.Happy
                            com.mindshield.app.domain.model.TwinMood.NEUTRAL -> TwinState.Neutral
                            com.mindshield.app.domain.model.TwinMood.TIRED -> TwinState.Tired
                            com.mindshield.app.domain.model.TwinMood.STRESSED -> TwinState.Stressed
                            com.mindshield.app.domain.model.TwinMood.EXHAUSTED -> TwinState.Stressed
                        },
                        level = uiState.twinState.level,
                        streak = uiState.twinState.streakDays,
                        healthProgress = uiState.twinState.healthLevel
                    )
                }

                // Screen Time Ring Chart
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Today's Screen Time",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(Modifier.height(8.dp))
                            UsageRingChart(
                                currentMinutes = uiState.totalScreenTimeMinutes.toInt(),
                                goalMinutes = uiState.goalMinutes.toInt(),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                // AI Insight Card
                item {
                    uiState.insight?.let { insight ->
                        InsightCard(
                            title = insight.title,
                            description = insight.description
                        )
                    }
                }

                // Top Apps
                item {
                    Text(
                        "Top Apps Today",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                items(uiState.topApps) { app ->
                    AppUsageBar(
                        appName = app.appName,
                        minutes = app.usageTimeMinutes.toInt(),
                        maxMinutes = uiState.goalMinutes.toInt()
                    )
                }

                // Quick Actions
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onNavigateToFocus,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("\uD83C\uDFAF Start Focus")
                        }
                        OutlinedButton(
                            onClick = onNavigateToInsights,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("\uD83D\uDCC8 View Insights")
                        }
                    }
                }
            }
        }
    }
}
