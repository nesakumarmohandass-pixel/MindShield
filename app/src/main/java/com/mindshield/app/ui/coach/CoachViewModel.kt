package com.mindshield.app.ui.coach

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindshield.app.data.repository.CoachRepository
import com.mindshield.app.data.repository.SubscriptionRepository
import com.mindshield.app.data.repository.UsageRepository
import com.mindshield.app.domain.model.CoachMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class CoachUiState(
    val messages: List<CoachMessage> = emptyList(),
    val isTyping: Boolean = false,
    val dailyMessagesUsed: Int = 0,
    val dailyMessageLimit: Int = 3,
    val isPremium: Boolean = false,
    val canSendMessage: Boolean = true
)

@HiltViewModel
class CoachViewModel @Inject constructor(
    private val coachRepository: CoachRepository,
    private val usageRepository: UsageRepository,
    private val subscriptionRepository: SubscriptionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CoachUiState())
    val uiState: StateFlow<CoachUiState> = _uiState.asStateFlow()

    init {
        loadChatHistory()
    }

    private fun loadChatHistory() {
        viewModelScope.launch {
            coachRepository.getChatHistory().collect { messages ->
                _uiState.value = _uiState.value.copy(
                    messages = messages,
                    canSendMessage = _uiState.value.isPremium || _uiState.value.dailyMessagesUsed < _uiState.value.dailyMessageLimit
                )
            }
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        if (!_uiState.value.canSendMessage) return

        viewModelScope.launch {
            // Save user message
            val userMsg = CoachMessage(
                id = UUID.randomUUID().toString(),
                content = text,
                isFromUser = true,
                timestamp = System.currentTimeMillis()
            )
            coachRepository.saveChatMessage(userMsg)

            val currentMessages = _uiState.value.messages + userMsg
            val newCount = _uiState.value.dailyMessagesUsed + 1
            _uiState.value = _uiState.value.copy(
                messages = currentMessages,
                isTyping = true,
                dailyMessagesUsed = newCount,
                canSendMessage = _uiState.value.isPremium || newCount < _uiState.value.dailyMessageLimit
            )

            // Build usage context
            val usageData = try {
                usageRepository.getTodayUsage().first()
            } catch (e: Exception) { emptyList() }
            val usageContext = usageData.take(5).joinToString(", ") { "${it.appName}: ${it.usageTimeMinutes}min" }

            // Get AI response
            val responseBuilder = StringBuilder()
            try {
                coachRepository.sendMessage(text, usageContext).collect { chunk ->
                    responseBuilder.append(chunk)
                }
            } catch (e: Exception) {
                responseBuilder.append("I'm having a moment of reflection. Could you try again? \uD83D\uDE4F")
            }

            val aiMsg = CoachMessage(
                id = UUID.randomUUID().toString(),
                content = responseBuilder.toString(),
                isFromUser = false,
                timestamp = System.currentTimeMillis()
            )
            coachRepository.saveChatMessage(aiMsg)

            _uiState.value = _uiState.value.copy(
                messages = _uiState.value.messages + aiMsg,
                isTyping = false
            )
        }
    }
}
