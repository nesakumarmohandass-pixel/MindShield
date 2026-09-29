package com.mindshield.app

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.mindshield.app.data.worker.UsageTrackWorker
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class MindShieldApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize RevenueCat
        Purchases.configure(
            PurchasesConfiguration.Builder(this, BuildConfig.REVENUECAT_API_KEY)
                .build()
        )

        setupPeriodicUsageTracking()
    }

    private fun setupPeriodicUsageTracking() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val usageTrackRequest = PeriodicWorkRequestBuilder<UsageTrackWorker>(1, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "usage_tracking",
            ExistingPeriodicWorkPolicy.KEEP,
            usageTrackRequest
        )
    }
}
