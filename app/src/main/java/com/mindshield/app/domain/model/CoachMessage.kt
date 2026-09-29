package com.mindshield.app.domain.model

data class CoachMessage(
    val id: String,
    val content: String,
    val isFromUser: Boolean,
    val timestamp: Long
)
