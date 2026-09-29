package com.mindshield.app.domain.model

data class UserGoal(
    val id: String,
    val title: String,
    val targetMinutes: Long,
    val currentMinutes: Long,
    val isActive: Boolean
)
