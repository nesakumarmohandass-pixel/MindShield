package com.mindshield.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindshield.app.data.repository.CoachRepository
import com.mindshield.app.data.repository.DigitalTwinRepository
import com.mindshield.app.data.repository.SubscriptionRepository
import com.mindshield.app.data.repository.UsageRepository
import com.mindshield.app.domain.model.AppUsage
import com.mindshield.app.domain.model.DigitalTwinState
import com.mindshield.app.domain.model.TwinMood
import com.mindshield.app.domain.model.TwinEnvironment
import com.mindshield.app.domain.model.WellnessInsight
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val greeting: String = "Good morning",
    val totalScreenTimeMinutes: Long = 0,
    val goalMinutes: Long = 240,
    val topApps: List<AppUsage> = emptyList(),
    val twinState: DigitalTwinState = DigitalTwinState(),
    val insight: WellnessInsight? = null,
    val isPremium: Boolean = false,
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val usageRepository: UsageRepository,
    private val coachRepository: CoachRepository,
    private val digitalTwinRepository: DigitalTwinRepository,
    private val subscriptionRepository: SubscriptionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            // Determine greeting based on time of day
            val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
            val greeting = when {
                hour < 12 -> "Good morning"
                hour < 17 -> "Good afternoon"
                else -> "Good evening"
            }

            usageRepository.getTodayUsage().collect { usageList ->
                val totalMinutes = usageList.sumOf { it.usageTimeMinutes }
                val goalMinutes = _uiState.value.goalMinutes
                val twinState = digitalTwinRepository.getTwinState(
                    todayUsage = totalMinutes,
                    goalMinutes = goalMinutes,
                    streakDays = 1 // TODO: track actual streaks
                )
                val insight = if (usageList.isNotEmpty()) {
                    coachRepository.getInsightOfTheDay(usageList)
                } else null

                _uiState.value = HomeUiState(
                    greeting = greeting,
                    totalScreenTimeMinutes = totalMinutes,
                    goalMinutes = goalMinutes,
                    topApps = usageList.take(5),
                    twinState = twinState,
                    insight = insight,
                    isPremium = false,
                    isLoading = false
                )
            }
        }
    }
}
