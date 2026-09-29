package com.mindshield.app.domain.model

enum class TwinMood {
    THRIVING, HAPPY, NEUTRAL, TIRED, STRESSED, EXHAUSTED
}

enum class TwinEnvironment {
    GARDEN, FOREST, MOUNTAIN, SPACE, CHAOS
}

data class DigitalTwinState(
    val healthLevel: Float = 1.0f,
    val mood: TwinMood = TwinMood.THRIVING,
    val streakDays: Int = 0,
    val level: Int = 1,
    val environment: TwinEnvironment = TwinEnvironment.GARDEN
)
