package com.mindshield.app.data.repository

import com.google.ai.client.generativeai.GenerativeModel
import com.mindshield.app.data.local.dao.CoachMessageDao
import com.mindshield.app.data.local.entities.CoachMessageEntity
import com.mindshield.app.domain.model.AppUsage
import com.mindshield.app.domain.model.CoachMessage
import com.mindshield.app.domain.model.InsightType
import com.mindshield.app.domain.model.WellnessInsight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class CoachRepository @Inject constructor(
    private val generativeModel: GenerativeModel,
    private val coachMessageDao: CoachMessageDao
) {
    private val systemPrompt = "You are MindShield Coach, a compassionate digital wellness companion. You NEVER shame users about their screen time. You help users understand their digital habits with empathy. You suggest alternatives, not restrictions. You ask thoughtful questions about feelings and triggers. You celebrate small wins and progress. Keep responses to 2-3 short paragraphs. Be warm, specific, and actionable."

    fun sendMessage(userMessage: String, usageContext: String): Flow<String> = flow {
        val prompt = "$systemPrompt\nContext: $usageContext\nUser: $userMessage\nCoach:"
        val response = generativeModel.generateContentStream(prompt)
        response.collect { chunk ->
            emit(chunk.text ?: "")
        }
    }

    fun getInsightOfTheDay(usageData: List<AppUsage>): WellnessInsight {
        val topApp = usageData.maxByOrNull { it.usageTimeMinutes }
        val insightType = if ((topApp?.usageTimeMinutes ?: 0) > 120) InsightType.WARNING else InsightType.CELEBRATION
        val title = if (insightType == InsightType.WARNING) "Mindful Check-in" else "Great Job!"
        val desc = "You've spent ${topApp?.usageTimeMinutes} mins on ${topApp?.appName}. Consider taking a quick break!"
        
        return WellnessInsight(
            id = UUID.randomUUID().toString(),
            title = title,
            description = desc,
            type = insightType,
            timestamp = System.currentTimeMillis()
        )
    }

    fun getChatHistory(): Flow<List<CoachMessage>> {
        return coachMessageDao.getRecentMessages().map { entities ->
            entities.map { entity ->
                CoachMessage(
                    id = entity.id.toString(),
                    content = entity.content,
                    isFromUser = entity.isFromUser,
                    timestamp = entity.timestamp
                )
            }
        }
    }

    suspend fun saveChatMessage(message: CoachMessage) {
        coachMessageDao.insertMessage(
            CoachMessageEntity(
                content = message.content,
                isFromUser = message.isFromUser,
                timestamp = message.timestamp
            )
        )
    }
}
