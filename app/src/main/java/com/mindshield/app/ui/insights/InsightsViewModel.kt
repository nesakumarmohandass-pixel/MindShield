package com.mindshield.app.ui.insights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindshield.app.data.repository.UsageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class InsightsUiState(
    val weeklyData: Map<String, Long> = emptyMap(),
    val dailyAverage: Long = 0,
    val isLoading: Boolean = true
)

@HiltViewModel
class InsightsViewModel @Inject constructor(
    private val usageRepository: UsageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InsightsUiState())
    val uiState: StateFlow<InsightsUiState> = _uiState.asStateFlow()

    init {
        loadInsights()
    }

    private fun loadInsights() {
        viewModelScope.launch {
            usageRepository.getWeeklyUsage().collect { weeklyMap ->
                val avg = if (weeklyMap.isNotEmpty()) weeklyMap.values.sum() / weeklyMap.size else 0L
                _uiState.value = InsightsUiState(
                    weeklyData = weeklyMap,
                    dailyAverage = avg,
                    isLoading = false
                )
            }
        }
    }
}
