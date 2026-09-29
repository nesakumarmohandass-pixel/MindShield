package com.mindshield.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mindshield.app.data.local.dao.CoachMessageDao
import com.mindshield.app.data.local.dao.UsageDao
import com.mindshield.app.data.local.entities.AppUsageEntity
import com.mindshield.app.data.local.entities.CoachMessageEntity

@Database(
    entities = [AppUsageEntity::class, CoachMessageEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usageDao(): UsageDao
    abstract fun coachMessageDao(): CoachMessageDao
}
