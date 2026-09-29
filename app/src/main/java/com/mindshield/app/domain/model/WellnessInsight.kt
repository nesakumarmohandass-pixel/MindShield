package com.mindshield.app.domain.model

enum class InsightType {
    TIP, WARNING, CELEBRATION, SUGGESTION
}

data class WellnessInsight(
    val id: String,
    val title: String,
    val description: String,
    val type: InsightType,
    val timestamp: Long
)
