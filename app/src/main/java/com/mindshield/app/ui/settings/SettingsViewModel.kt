package com.mindshield.app.ui.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindshield.app.data.repository.SubscriptionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val dailyGoalHours: Int = 4,
    val notificationsEnabled: Boolean = true,
    val darkModeEnabled: Boolean = false,
    val isPremium: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val subscriptionRepository: SubscriptionRepository
) : ViewModel() {

    companion object {
        val DAILY_GOAL_KEY = intPreferencesKey("daily_goal_hours")
        val NOTIFICATIONS_KEY = booleanPreferencesKey("notifications_enabled")
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
    }

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            val prefs = dataStore.data.first()
            _uiState.value = SettingsUiState(
                dailyGoalHours = prefs[DAILY_GOAL_KEY] ?: 4,
                notificationsEnabled = prefs[NOTIFICATIONS_KEY] ?: true,
                darkModeEnabled = prefs[DARK_MODE_KEY] ?: false,
                isPremium = false
            )
        }
    }

    fun updateGoal(hours: Int) {
        viewModelScope.launch {
            dataStore.edit { it[DAILY_GOAL_KEY] = hours }
            _uiState.value = _uiState.value.copy(dailyGoalHours = hours)
        }
    }

    fun toggleNotifications() {
        viewModelScope.launch {
            val newValue = !_uiState.value.notificationsEnabled
            dataStore.edit { it[NOTIFICATIONS_KEY] = newValue }
            _uiState.value = _uiState.value.copy(notificationsEnabled = newValue)
        }
    }

    fun toggleDarkMode() {
        viewModelScope.launch {
            val newValue = !_uiState.value.darkModeEnabled
            dataStore.edit { it[DARK_MODE_KEY] = newValue }
            _uiState.value = _uiState.value.copy(darkModeEnabled = newValue)
        }
    }
}
