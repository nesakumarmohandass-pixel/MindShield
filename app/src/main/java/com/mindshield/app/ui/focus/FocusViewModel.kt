package com.mindshield.app.ui.focus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FocusUiState(
    val isActive: Boolean = false,
    val totalSeconds: Int = 25 * 60,
    val remainingSeconds: Int = 25 * 60,
    val selectedPreset: Int = 25,
    val sessionsCompleted: Int = 0,
    val isBreathing: Boolean = false
)

@HiltViewModel
class FocusViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(FocusUiState())
    val uiState: StateFlow<FocusUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    fun setPreset(minutes: Int) {
        if (!_uiState.value.isActive) {
            _uiState.value = _uiState.value.copy(
                selectedPreset = minutes,
                totalSeconds = minutes * 60,
                remainingSeconds = minutes * 60
            )
        }
    }

    fun toggleTimer() {
        if (_uiState.value.isActive) {
            stopTimer()
        } else {
            startTimer()
        }
    }

    private fun startTimer() {
        _uiState.value = _uiState.value.copy(isActive = true)
        timerJob = viewModelScope.launch {
            while (_uiState.value.remainingSeconds > 0 && _uiState.value.isActive) {
                delay(1000)
                _uiState.value = _uiState.value.copy(
                    remainingSeconds = _uiState.value.remainingSeconds - 1
                )
            }
            if (_uiState.value.remainingSeconds <= 0) {
                _uiState.value = _uiState.value.copy(
                    isActive = false,
                    sessionsCompleted = _uiState.value.sessionsCompleted + 1,
                    remainingSeconds = _uiState.value.totalSeconds
                )
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        _uiState.value = _uiState.value.copy(isActive = false)
    }

    fun toggleBreathing() {
        _uiState.value = _uiState.value.copy(
            isBreathing = !_uiState.value.isBreathing
        )
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
