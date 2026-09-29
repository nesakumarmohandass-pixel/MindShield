package com.mindshield.app.data.repository

import com.mindshield.app.domain.model.DigitalTwinState
import com.mindshield.app.domain.model.TwinEnvironment
import com.mindshield.app.domain.model.TwinMood
import javax.inject.Inject

class DigitalTwinRepository @Inject constructor() {

    fun getTwinState(todayUsage: Long, goalMinutes: Long, streakDays: Int): DigitalTwinState {
        val ratio = if (goalMinutes > 0) todayUsage.toFloat() / goalMinutes.toFloat() else 0f
        
        val (mood, environment) = when {
            ratio < 0.5f -> Pair(TwinMood.THRIVING, TwinEnvironment.GARDEN)
            ratio < 0.75f -> Pair(TwinMood.HAPPY, TwinEnvironment.FOREST)
            ratio < 1.0f -> Pair(TwinMood.NEUTRAL, TwinEnvironment.MOUNTAIN)
            ratio < 1.5f -> Pair(TwinMood.TIRED, TwinEnvironment.SPACE)
            else -> Pair(TwinMood.STRESSED, TwinEnvironment.CHAOS)
        }
        
        val healthLevel = (1f - (ratio / 2f)).coerceIn(0f, 1f)
        val level = (streakDays / 7) + 1
        
        return DigitalTwinState(
            healthLevel = healthLevel,
            mood = mood,
            streakDays = streakDays,
            level = level,
            environment = environment
        )
    }
}
