package com.mindshield.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mindshield.app.data.local.entities.AppUsageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsage(entity: AppUsageEntity)

    @Query("SELECT * FROM app_usage WHERE date = :date")
    fun getUsageForDate(date: String): Flow<List<AppUsageEntity>>

    @Query("SELECT SUM(usageTimeMinutes) FROM app_usage WHERE date = :date")
    fun getTotalUsageForDate(date: String): Flow<Long?>

    @Query("SELECT * FROM app_usage WHERE date >= :startDate AND date <= :endDate")
    fun getUsageForDateRange(startDate: String, endDate: String): Flow<List<AppUsageEntity>>

    @Query("DELETE FROM app_usage WHERE lastUsed < :timestamp")
    suspend fun deleteOlderThan(timestamp: Long)
}
