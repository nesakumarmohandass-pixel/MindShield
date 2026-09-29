package com.mindshield.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mindshield.app.data.local.entities.CoachMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoachMessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(entity: CoachMessageEntity)

    @Query("SELECT * FROM coach_messages ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentMessages(limit: Int = 50): Flow<List<CoachMessageEntity>>

    @Query("DELETE FROM coach_messages")
    suspend fun clearHistory()
}
