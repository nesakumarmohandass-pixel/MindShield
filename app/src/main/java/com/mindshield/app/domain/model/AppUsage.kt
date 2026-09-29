package com.mindshield.app.domain.model

import android.graphics.drawable.Drawable

data class AppUsage(
    val packageName: String,
    val appName: String,
    val usageTimeMinutes: Long,
    val lastUsed: Long,
    val category: String,
    val iconDrawable: Drawable?
)
