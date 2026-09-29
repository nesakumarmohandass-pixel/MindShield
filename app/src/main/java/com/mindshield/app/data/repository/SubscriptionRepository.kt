package com.mindshield.app.data.repository

import android.app.Activity
import com.mindshield.app.domain.model.SubscriptionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SubscriptionRepository @Inject constructor() {
    
    // In a real app, this would use RevenueCat's Purchases object
    fun getSubscriptionState(): Flow<SubscriptionState> = flow {
        // Stub implementation
        emit(SubscriptionState.Free)
    }

    fun getOfferings(): List<String> {
        // Stub implementation
        return listOf("monthly", "annual")
    }

    fun purchasePackage(activity: Activity, pkg: String): Boolean {
        // Stub implementation
        return true
    }

    fun restorePurchases(): Boolean {
        // Stub implementation
        return true
    }

    fun isPremium(): Flow<Boolean> = flow {
        emit(false)
    }
}
