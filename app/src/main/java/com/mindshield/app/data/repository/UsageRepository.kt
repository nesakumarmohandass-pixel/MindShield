package com.mindshield.app.data.repository

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import com.mindshield.app.data.local.dao.UsageDao
import com.mindshield.app.data.local.entities.AppUsageEntity
import com.mindshield.app.domain.model.AppUsage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class UsageRepository @Inject constructor(
    private val context: Context,
    private val usageDao: UsageDao
) {
    private val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    private val packageManager = context.packageManager

    fun getTodayUsage(): Flow<List<AppUsage>> = flow {
        val (start, end) = getTodayRange()
        val stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start, end)
        
        val usages = stats.mapNotNull { stat ->
            if (stat.totalTimeInForeground > 0) {
                try {
                    val appInfo = packageManager.getApplicationInfo(stat.packageName, 0)
                    val appName = packageManager.getApplicationLabel(appInfo).toString()
                    val icon = packageManager.getApplicationIcon(appInfo)
                    val category = "Unknown" // Can map using App categories if needed
                    AppUsage(stat.packageName, appName, stat.totalTimeInForeground / 60000, stat.lastTimeUsed, category, icon)
                } catch (e: PackageManager.NameNotFoundException) {
                    null
                }
            } else null
        }.sortedByDescending { it.usageTimeMinutes }
        
        emit(usages)
    }

    fun getTotalScreenTimeToday(): Flow<Long> = flow {
        val (start, end) = getTodayRange()
        val stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start, end)
        val total = stats.sumOf { it.totalTimeInForeground } / 60000
        emit(total)
    }

    fun getWeeklyUsage(): Flow<Map<String, Long>> = flow {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val end = System.currentTimeMillis()
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val start = calendar.timeInMillis
        
        val stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start, end)
        val map = stats.groupBy { 
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it.firstTimeStamp))
        }.mapValues { entry -> 
            entry.value.sumOf { it.totalTimeInForeground } / 60000 
        }
        emit(map)
    }

    suspend fun saveUsageSnapshot() {
        val (start, end) = getTodayRange()
        val stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start, end)
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        
        stats.forEach { stat ->
            if (stat.totalTimeInForeground > 0) {
                try {
                    val appInfo = packageManager.getApplicationInfo(stat.packageName, 0)
                    val appName = packageManager.getApplicationLabel(appInfo).toString()
                    val entity = AppUsageEntity(
                        packageName = stat.packageName,
                        appName = appName,
                        usageTimeMinutes = stat.totalTimeInForeground / 60000,
                        date = todayStr,
                        lastUsed = stat.lastTimeUsed
                    )
                    usageDao.insertUsage(entity)
                } catch (e: PackageManager.NameNotFoundException) {
                    // Ignore
                }
            }
        }
    }

    private fun getTodayRange(): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        return Pair(calendar.timeInMillis, System.currentTimeMillis())
    }
}
