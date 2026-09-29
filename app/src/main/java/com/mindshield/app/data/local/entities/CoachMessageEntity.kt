package com.mindshield.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coach_messages")
data class CoachMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val content: String,
    val isFromUser: Boolean,
    val timestamp: Long
)
